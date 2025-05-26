let user;

window.onload = async function () {
  preventBackCache();

  // Session check
  let response = await fetch(`${domain}/session`);
  let responseBody = await response.json();

  if (!responseBody.success) {
    window.location = "../"; // Redirect to login
    return;
  }

  // Set title of page to "*User's first name*'s Dashboard"
  user = responseBody.data;
  localStorage.setItem("firstname", user.firstname);
  document.title = `${user.firstname}'s Dashboard`;

  // Logout Function
  const logoutBtn = document.getElementById("logout-btn");
  if (logoutBtn) {
    logoutBtn.addEventListener("click", handleLogout);
  }

  // Show all lists
  await getAllLists();
};

// Prevent showing page when going back after logging out
function preventBackCache() {
  window.addEventListener("pageshow", function (event) {
    if (event.persisted || window.performance.getEntriesByType("navigation")[0]?.type === "back_forward") {
      window.location.reload();
    }
  });
}

// Delete session when logging out
async function handleLogout() {
  try {
    await fetch(`${domain}/session`, { method: "DELETE" });
    localStorage.clear();
    window.location.href = "../"; // Go back to login page
  } catch (err) {
    alert("Logout failed.");
    console.error(err);
  }
}

// Show all of User's list and set URL
async function getAllLists() {
  let response = await fetch(`${domain}/list?userId=${user.id}`);
  let responseBody = await response.json();
  let lists = responseBody.data;

  lists.forEach((list) => {
    createList(list);
  });
}

// Creating a list
function createList(list) {
  let listContainerElem = document.getElementById("list-container");

  let listCardElem = document.createElement("div");
  listCardElem.className = "list-card";

  listCardElem.innerHTML = `
    <div class="list-title">${list.name}</div>
    <div class="img-container"><img src="./cart.png" alt="cart"></div>
    <div class="list-btns">
      <button id="view-btn-${list.id}" class="btn btn-primary" onclick="viewList(event)">View</button>
      <button id="delete-btn-${list.id}" class="delete-btn" onclick="deleteList(event)"><div>x</div></button>
    </div>`;

  listContainerElem.appendChild(listCardElem);
}

async function createNewList(event) {
  event.preventDefault();

  let createListInputElem = document.getElementById("list-name");
  let list = { id: 0, name: createListInputElem.value, userId: user.id };

  let response = await fetch(`${domain}/list`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(list),
  });

  if (!response.ok) {
    alert("Failed to create list.");
    return;
  }

  await response.json();
  createListInputElem.value = "";
  document.getElementById("list-container").innerHTML = "";
  await getAllLists();
}

// deleting a list
async function deleteList(event) {
  let elem = event.target.className != "delete-btn" ? event.target.parentNode : event.target;
  let listId = elem.id.substring("delete-btn-".length);

  let response = await fetch(`${domain}/list/${listId}`, { method: "DELETE" });
  let responseBody = await response.json();

  let listElem = elem.parentNode.parentNode;
  listElem.remove();

  console.log(responseBody);
}

function viewList(event) {
  if (!user || !user.id) {
    alert("User not logged in");
    return;
  }

  let viewBtn = event.target;
  let listId = viewBtn.id.substring("view-btn-".length);

  window.location = `../grocerylist/?userId=${user.id}&listId=${listId}`;
}
