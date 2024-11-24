// Obtiene los elementos del documento HTML por su ID
let searchBar = document.getElementById("searchBar");
let searchButton = document.getElementById("searchButton");

// Función para habilitar/deshabilitar el botón de búsqueda
const toggleSearchButton = () => {
  // Verifica si la barra de búsqueda está vacía después de eliminar espacios en blanco
  if (searchBar.value.trim() === "") {
    // Deshabilita el boton de busqueda
    searchButton.disabled = true;
  } else {
    // De lo contrario, lo habilita
    searchButton.disabled = false;
  }
};

// Escucha el evento 'input' en la barra de búsqueda
// El evento input se dispara cuando el valor (value) de un elemento <input>, <select>, o
// <textarea> ha sido cambiado.
searchBar.addEventListener("input", toggleSearchButton);

// Llamada inicial para configurar el estado del botón
// Es necesario llamarlo o de lo contrario no puede evaluar si hay texto en el <input> de
// la barra de busqueda al cargar la pagina web
toggleSearchButton();
