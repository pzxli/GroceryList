window.onload = async function () {

  const isLoggedIn = localStorage.getItem("firstname") && localStorage.getItem("userId");
  if (!isLoggedIn) {
    alert("You must be logged in to access this page.");
    window.location.replace("/");
    return;
  }

  const params = new URLSearchParams(window.location.search);
  const userId = params.get("userId");
  const listId = parseInt(params.get("listId"));
  const firstName = localStorage.getItem("firstname") || "User";

// Prevent back button from showing cached content
window.addEventListener("pageshow", function (event) {
  if (event.persisted) {
    window.location.reload();
  }
});


  if (!userId || !listId) {
    alert("Missing userId or listId in URL");
    return;
  }

  try {
    const response = await fetch(`${domain}/list?userId=${userId}`);
    const data = await response.json();
    const lists = data.data;

    const list = lists.find((l) => l.id === listId);
    if (!list) throw new Error("List not found for this user.");

    document.title = `${firstName}’s ${list.name} Grocery List`;
  } catch (err) {
    console.error("Failed to load list:", err);
    document.title = "Grocery List";
  }

  try {
    await loadGroceryItems(listId);
  } catch (err) {
    alert("Error loading grocery list. Please try again later.");
    console.error(err);
  }


  // Inline "+" button submission
  const inlineAddBtn = document.getElementById("add-item-btn");
  if (inlineAddBtn) {
    const inlineNameInput = document.getElementById("inline-item-name");
    const inlineQuantityInput = document.getElementById("inline-item-quantity");

    inlineAddBtn.addEventListener("click", async function () {
      await addNewItem(listId, inlineNameInput, inlineQuantityInput);
    });
  }
};

async function loadGroceryItems(listId) {
  const response = await fetch(`${domain}/item?listId=${listId}`);
  const responseBody = await response.json();

  if (!responseBody.success) {
    throw new Error(responseBody.message || "Failed to load items");
  }

  renderItems(responseBody.data);
}

function renderItems(items) {
  const container = document.querySelector(".item-container");
  container.innerHTML = "";

  if (items.length === 0) {
    container.innerHTML = "<p>No items found in this list.</p>";
    return;
  }

  items.forEach((item) => {
    const itemDiv = document.createElement("div");
    itemDiv.className = "item";

    itemDiv.innerHTML = `
      <h5 class="item-name">${item.name}</h5>
      <p>Quantity: ${item.quantity}</p>
      <button class="btn btn-danger" data-item-id="${item.id}">Delete</button>
    `;

    const deleteBtn = itemDiv.querySelector("button");
    deleteBtn.addEventListener("click", () => deleteItem(item.id, itemDiv));

    container.appendChild(itemDiv);
  });
}

async function addNewItem(listId, nameInput, quantityInput) {
  if (!nameInput || !quantityInput) return;

  const name = nameInput.value.trim();
  const quantity = parseInt(quantityInput.value);

  console.log("Name:", name, "Quantity raw:", quantityInput.value, "Parsed:", quantity);

  if (!name) {
    alert("Item name cannot be empty.");
    return;
  }

  if (quantityInput.value.trim() === "" || isNaN(quantity) || quantity <= 0) {
    alert("Please enter a valid quantity (must be a number greater than 0).");
    return;
  }

  const newItem = {
    name,
    quantity,
    listId: parseInt(listId),
  };

  try {
    const response = await fetch(`${domain}/item`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(newItem),
    });

    const responseBody = await response.json();

    if (!responseBody.success) {
      alert("Failed to add item: " + responseBody.message);
      return;
    }

    nameInput.value = "";
    quantityInput.value = "";

    await loadGroceryItems(listId);
  } catch (err) {
    alert("Error adding item. Please try again later.");
    console.error(err);
  }
}

async function deleteItem(itemId, itemDiv) {
  if (!confirm("Are you sure you want to delete this item?")) return;

  try {
    const response = await fetch(`${domain}/item/${itemId}`, {
      method: "DELETE",
    });

    const responseBody = await response.json();

    if (!responseBody.success) {
      alert("Failed to delete item: " + responseBody.message);
      return;
    }

    itemDiv.remove();
  } catch (err) {
    alert("Error deleting item. Please try again later.");
    console.error(err);
  }
}

// Logout
document.addEventListener("DOMContentLoaded", () => {
  const logoutBtn = document.getElementById("logout-btn");
  if (logoutBtn) {
    logoutBtn.addEventListener("click", async function () {
      try {
        await fetch(`${domain}/session`, { method: "DELETE" });
        localStorage.clear();
        // Prevent back button from accessing cached pages
        window.location.replace("/");
      } catch (err) {
        alert("Logout failed.");
        console.error(err);
      }
    });
  }
});
