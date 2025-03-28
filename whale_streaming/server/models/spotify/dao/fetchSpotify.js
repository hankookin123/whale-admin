// Spotify API에 정보를 요청 및 응답하는 함수
async function fetchWebApi(req, endpoint, method, body) {
    const response = await fetch(`https://api.spotify.com/${endpoint}`, {
        headers: {
            Authorization: `Bearer ${req.session.accessToken}`,
        },
        method,
        body:JSON.stringify(body)
    });
    return await response.json();
}

module.exports = {
    fetchWebApi
};