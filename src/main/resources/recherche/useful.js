// Cette méthode permet de débloquer l'url d'une vidéo
function unlockPlayerResponse(playerResponse) {
    const videoId = playerResponse.videoDetails.videoId;
    const reason = playerResponse.playabilityStatus?.status;
    const unlockedPayerResponse = getUnlockedPlayerResponse(videoId, reason);

    // account proxy error?
    if (unlockedPayerResponse.errorMessage) {
        Notification.show("Unable to unlock this video 🙁 - More information in the developer console (ProxyError)", 10);
        throw new Error(`Unlock Failed, errorMessage:${unlockedPayerResponse.errorMessage}; innertubeApiKey:${INNERTUBE_CONFIG.INNERTUBE_API_KEY}; innertubeClientName:${INNERTUBE_CONFIG.INNERTUBE_CLIENT_NAME}; innertubeClientVersion:${INNERTUBE_CONFIG.INNERTUBE_CLIENT_VERSION}`);
    }

    // check if the unlocked response isn't playable
    if (unlockedPayerResponse.playabilityStatus?.status !== "OK") {
        Notification.show(`Unable to unlock this video 🙁 - More information in the developer console (playabilityStatus: ${unlockedPayerResponse.playabilityStatus?.status})`, 10);
        throw new Error(`Unlock Failed, playabilityStatus:${unlockedPayerResponse.playabilityStatus?.status}; innertubeApiKey:${INNERTUBE_CONFIG.INNERTUBE_API_KEY}; innertubeClientName:${INNERTUBE_CONFIG.INNERTUBE_CLIENT_NAME}; innertubeClientVersion:${INNERTUBE_CONFIG.INNERTUBE_CLIENT_VERSION}`);
    }

    // if the video info was retrieved via proxy, store the URL params from the url- or signatureCipher-attribute to detect later if the requested video files are from this unlock.
    // => see isUnlockedByAccountProxy()
    if (unlockedPayerResponse.proxied && unlockedPayerResponse.streamingData?.adaptiveFormats) {
        const cipherText = unlockedPayerResponse.streamingData.adaptiveFormats.find(x => x.signatureCipher)?.signatureCipher;
        const videoUrl = cipherText ? new URLSearchParams(cipherText).get("url") : unlockedPayerResponse.streamingData.adaptiveFormats.find(x => x.url)?.url;

        lastProxiedGoogleVideoUrlParams = videoUrl ? new URLSearchParams(new URL(videoUrl).search) : null;
    }

    Notification.show("Video successfully unlocked!");

    return unlockedPayerResponse;
}

function getUnlockedPlayerResponse(videoId, reason) {
    // Check if response is cached
    if (responseCache.videoId === videoId) return responseCache.playerResponse;

    setInnertubeConfigFromYtcfg();

    let playerResponse = useInnertubeEmbed();

    if (playerResponse?.playabilityStatus?.status !== "OK") playerResponse = useProxy();

    // Cache response for 10 seconds
    responseCache = { videoId, playerResponse };
    setTimeout(() => { responseCache = {} }, 10000);

    return playerResponse;

    // Strategy 1: Retrieve the video info by using a age-gate bypass for the innertube API
    // Source: https://github.com/yt-dlp/yt-dlp/issues/574#issuecomment-887171136
    function useInnertubeEmbed() {
        console.info("Simple-YouTube-Age-Restriction-Bypass: Trying Unlock Method #1 (Innertube Embed)");
        const payload = getInnertubeEmbedPayload(videoId);
        const xmlhttp = new XMLHttpRequest();
        xmlhttp.open("POST", `/youtubei/v1/player?key=${INNERTUBE_CONFIG.INNERTUBE_API_KEY}`, false); // Synchronous!!!
        xmlhttp.send(JSON.stringify(payload));
        return nativeParse(xmlhttp.responseText);
    }

    // Strategy 2: Retrieve the video info from an account proxy server.
    // See https://github.com/zerodytrash/Simple-YouTube-Age-Restriction-Bypass/tree/main/account-proxy
    function useProxy() {
        console.info("Simple-YouTube-Age-Restriction-Bypass: Trying Unlock Method #2 (Account Proxy)");
        const xmlhttp = new XMLHttpRequest();
        xmlhttp.open("GET", ACCOUNT_PROXY_SERVER_HOST + `/getPlayer?videoId=${encodeURIComponent(videoId)}&reason=${encodeURIComponent(reason)}&clientName=${INNERTUBE_CONFIG.INNERTUBE_CLIENT_NAME}&clientVersion=${INNERTUBE_CONFIG.INNERTUBE_CLIENT_VERSION}&signatureTimestamp=${INNERTUBE_CONFIG.STS}`, false); // Synchronous!!!
        xmlhttp.send(null);
        const playerResponse = nativeParse(xmlhttp.responseText);
        playerResponse.proxied = true;
        return playerResponse;
    }
}