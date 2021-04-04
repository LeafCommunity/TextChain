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

function getTreeApiUrl(sha)
{
    return `https://api.github.com/repos/${project.github.user}/${project.github.repo}/git/trees/${sha}`;
}

async function fetchTreeOrFail(sha)
{
    console.log(`Fetching tree: ${sha}`);

    let treeRequest = await fetch(getTreeApiUrl(sha));
    console.log(treeRequest);

    if (treeRequest.status === 200) { return await treeRequest.json(); }

    const statusResponses =
    {
        403: "Forbidden (likely ratelimited - check back later)",
        404: `Not Found (<span class="monospace">${sha}</span>)`
    };
    
    showError(treeRequest.status, `<p>${statusResponses[treeRequest.status] || treeRequest.statusText}</p>`);
    throw treeRequest;
}

async function fetchRootTree()
{
    return await fetchTreeOrFail(project.github.branch);
}

async function fetchProjectsTree()
{
    for (let rootNode of (await fetchRootTree()).tree)
    {
        if (rootNode.path === "javadocs")
        {
            return await fetchTreeOrFail(rootNode.sha);
        }
    }
    throw new Error("Missing javadocs directory.");
}

async function fetchVersionsByProjectTree(projectNode)
{
    let versions = [];

    for (let versionNode of (await fetchTreeOrFail(projectNode.sha)).tree)
    {
        versions.unshift({
            name: versionNode.path,
            path: `javadocs/${projectNode.path}/${versionNode.path}/index.html`
        });
    }

    return {
        name: projectNode.path,
        versions: versions
    };
}

async function fetchProjectVersions()
{
    let projects = [];

    for (let projectNode of (await fetchProjectsTree()).tree)
    {
        projects.push(await fetchVersionsByProjectTree(projectNode));
    }

    return projects.sort((a, b) => a.name.localeCompare(b.name));
}

async function getOrFetchProjectVersions()
{
    let existing = window.localStorage.getItem("javadocsProjectListCache");

    if (existing)
    {
        let cached = JSON.parse(existing);
        let millis = Date.now() - cached.timestamp;

        if (millis < 7200000) // valid for 2 hours
        {
            console.log("Using cached javadocs list.")
            return cached.projects;
        }

        console.log("Cached javadocs list has expired.")
    }

    console.log("Fetching and caching javadocs list...")

    let uncached = 
    {
        timestamp: Date.now(),
        projects: await fetchProjectVersions()
    };

    window.localStorage.setItem("javadocsProjectListCache", JSON.stringify(uncached));
    loadStatus.message = "Loaded fresh from the API in ";
    return uncached.projects;
}

function renderVersionsList(versions)
{
    return versions.map(ver => `<li><a href="${ver.path}" target="_blank"><span class="monospace">${ver.name}</span></a></li>`).join("");
}

async function renderProjects()
{
    let html = "";

    for (let proj of await getOrFetchProjectVersions())
    {
        html += 
            `<div class="project-card">
                <div class="project-title">
                    <h2>${proj.name}</h2>
                </div>
                <div class="versions-list">
                    <ul>${renderVersionsList(proj.versions)}</ul>
                </div>
            </div>`;
    }

    return html;
}

async function displayJavadocsList()
{
    document.getElementById("docs-list").innerHTML = await renderProjects();
    document.getElementById("loading-display").classList.add("hidden");
    document.getElementById("load-status").innerText = `${loadStatus.message} ${Date.now() - loadStatus.start} ms.`;
}

function index()
{
    const init = () =>
    {
        document.title = project.name + " / Javadocs";

        document.querySelector("#header-text h1 .bold").innerText = project.name;
        document.querySelector("#github-link").href = `https://github.com/${project.github.user}/${project.github.repo}`;

        showErrorByQuery();
        displayJavadocsList();

        document.getElementById("copyright").innerText = `Copyright © RezzedUp ${new Date().getFullYear()}.`;
    }

    document.onreadystatechange = event =>
    {
        if (event.target.readyState === "interactive") { init(); }
    }
}
