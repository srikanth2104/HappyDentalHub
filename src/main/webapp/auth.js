(function () {
  // ---- styles for the buttons (does not touch your H1.css) ----
  var css = document.createElement("style");
  css.textContent =
    "#authBox{position:absolute;right:16px;top:50%;transform:translateY(-50%);" +
    "display:flex;align-items:center;gap:10px;z-index:50;}" +
    ".auth-btn{padding:6px 16px;border-radius:8px;border:2px solid #2c7a7b;" +
    "background:#2c7a7b;color:#fff;font-size:15px;font-weight:600;text-decoration:none;" +
    "cursor:pointer;transition:all .3s ease;white-space:nowrap;}" +
    ".auth-btn:hover{background:#fff;color:#2c7a7b;}" +
    ".auth-btn.outline{background:#fff;color:#2c7a7b;}" +
    ".auth-btn.outline:hover{background:#2c7a7b;color:#fff;}" +
    ".auth-profile{color:#2c7a7b;font-weight:700;text-decoration:none;white-space:nowrap;}" +
    ".auth-profile:hover{color:palevioletred;}" +
    "@media (max-width:768px){#authBox{position:static;transform:none;justify-content:center;padding:8px 0;}}";
  document.head.appendChild(css);

  function render(s) {
    var host = document.querySelector(".nav") || document.querySelector(".header") || document.body;
    if (getComputedStyle(host).position === "static") host.style.position = "relative";

    var box = document.createElement("div");
    box.id = "authBox";

    if (s && s.loggedIn) {
      var first = (s.name || "User").split(" ")[0];
      box.innerHTML =
        '<a class="auth-profile" href="profile.html" title="My profile">👤 ' + escapeHtml(first) + '</a>' +
        '<a class="auth-btn outline" href="logout">Logout</a>';
    } else {
      box.innerHTML = '<a class="auth-btn" href="login.html">🔑 Login</a>';
    }
    host.appendChild(box);
  }

  function escapeHtml(t) {
    return String(t).replace(/[&<>"']/g, function (c) {
      return { "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;" }[c];
    });
  }

  fetch("authStatus", { credentials: "same-origin", cache: "no-store" })
    .then(function (r) { return r.json(); })
    .then(render)
    .catch(function () { render({ loggedIn: false }); });
})();