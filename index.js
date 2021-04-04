const project = 
{
    name: "TextChain",
    github: 
    {
        user: "LeafCommunity",
        repo: "TextChain",
        branch: "gh-pages"
    }
};

let loadStatus =
{
    start: Date.now(),
    message: "Loaded from cache in "
};

function getQueryByName(name, url = window.location.search)
{
    name = name.replace(/[\[\]]/g, "\\$&");
    let regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)");
    let results = regex.exec(url);

    if (!results) { return null; }
    if (!results[2]) { return ""; }

    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

function showError(errorCode, html)
{
    let errorDisplay = document.getElementById("error-display");
    errorDisplay.classList.remove("hidden");
    errorDisplay.innerHTML = `<h1>${errorCode}</h1>${html || ""}`;
}

function showErrorByQuery()
{
    let error = getQueryByName("error");

    if (error === "404")
    {
        showError(error, `<p>Unknown Page: <span class="monospace">${window.location.hostname + window.location.hash.replace(/^#/, "")}</span></p>`);
    }
    else if (error)
    { 
        showError(error); 
    }
}

function getContentsApiUrl(path)
{
    return `https://api.github.com/repos/${project.github.user}/${project.github.repo}/contents/${path}/?ref=${project.github.branch}`
}

async function gatherAvailableVersions(dir)
{
    let versionsRequest = await fetch(getContentsApiUrl(`javadocs/${dir}`));
    let versionDirContents = await versionsRequest.json();

    let versionsList = [];

    for (let version of versionDirContents)
    {
        versionsList.unshift(`<li><a href="${version.path}" target="_blank"><span class="monospace">${version.name}</span></a></li>`)
    }

    return versionsList.join("");
}

async function requestThenCacheJavadocs()
{
    let javadocsRequest = await fetch(getContentsApiUrl("javadocs"));

    if (javadocsRequest.status !== 200)
    {
        console.error(javadocsRequest);
        let statusText = (javadocsRequest.status === 403) ? "Forbidden (likely ratelimited - check back later)" : javadocsRequest.statusText;
        showError(javadocsRequest.status, `<p>${statusText}</p>`);
    }

    let javadocsDirContents = await javadocsRequest.json();

    let docsList = [];
    let pendingRequests = [];

    for (let dir of javadocsDirContents)
    {
        if (dir.type !== "dir") { continue; }

        let pending = gatherAvailableVersions(dir.name)
            .then(availableVersions => 
            {
                let html =
                    `<div class="project-card">
                        <div class="project-title">
                            <h2>${dir.name}</h2>
                        </div>
                        <div class="versions-list">
                            <ul>${availableVersions}</ul>
                        </div>
                    </div>`;
                
                docsList.push({name: dir.name, html: html});
            });

        pendingRequests.push(pending);
    }

    await Promise.all(pendingRequests);

    let cached =
    {
        timestamp: Date.now(),
        docs: docsList.sort((a, b) => a.name.localeCompare(b.name))
    };
    
    window.localStorage.setItem("javadocsListCache", JSON.stringify(cached));
    loadStatus.message = "Loaded fresh from the API in ";
    return cached;
}

function displayJavadocsList(docsList)
{
    document.getElementById("docs-list").innerHTML = docsList.map(docs => docs.html).join("");
    document.getElementById("loading-display").classList.add("hidden");
    document.getElementById("load-status").innerText = `${loadStatus.message} ${Date.now() - loadStatus.start} ms.`;
}

async function loadJavadocsList()
{
    let existing = window.localStorage.getItem("javadocsListCache");

    if (existing)
    {
        let cached = JSON.parse(existing);
        let millis = Date.now() - cached.timestamp;

        if (millis < 7200000) // valid for 2 hours
        {
            console.log("Using cached javadocs list.")
            displayJavadocsList(cached.docs);
            return;
        }

        console.log("Cached javadocs list has expired.")
    }

    console.log("Fetching javadocs list...")

    let fetched = await requestThenCacheJavadocs();
    displayJavadocsList(fetched.docs);
}

function index()
{
    const init = () =>
    {
        document.title = project.name + " / Javadocs";

        document.querySelector("#header-text h1 .bold").innerText = project.name;
        document.querySelector("#github-link").href = `https://github.com/${project.github.user}/${project.github.repo}`;

        showErrorByQuery();
        loadJavadocsList();

        document.getElementById("copyright").innerText = `Copyright © RezzedUp ${new Date().getFullYear()}.`;
    }

    document.onreadystatechange = event =>
    {
        if (event.target.readyState === "interactive") { init(); }
    }
}
