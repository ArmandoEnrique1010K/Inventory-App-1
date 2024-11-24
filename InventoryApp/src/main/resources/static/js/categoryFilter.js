// Función para buscar productos basada en la categoría seleccionada y la palabra clave
const findProducts = () => {
  // Previene el envío predeterminado del formulario
  // Por defecto el boton de tipo submit de un formulario lleva al usuario hacia otra pagina, pero expone
  // los parametros y valores del formulario en la URL
  event.preventDefault();

  // Construir la URL con los parámetros seleccionados
  let url = "/products/search?";

  // Obtiene el valor de la palabra clave ingresada
  const wordInput = document.querySelector('input[name="word"]').value;

  // Obtiene el valor seleccionado para la categoría
  const categorySelected = document.querySelector(
    'input[name="category"]:checked'
  );

  // Añadir la palabra clave a la URL si está presente
  if (wordInput) {
    url = url + `word=${wordInput}&`;
  }

  // Añadir la categoría seleccionada a la URL si está presente
  // Primera forma:
  // if (categorySelected) {
  //   // Solamente si la categoria tiene un valor diferente que 0 (El valor 0 representa todas las categoria)
  //   if (categorySelected.value !== "0") {
  //     url = url + `categoryId=${categorySelected.value}&`;
  //   } else {
  //     url = url + `categoryId=`;
  //   }
  // }

  // Segunda forma:
  if (categorySelected && categorySelected.value !== "0") {
    url += `categoryId=${categorySelected.value}&`;
  }

  // Elimina el último carácter '&' si está presente
  if (url.endsWith("&")) {
    url = url.slice(0, -1);
  }

  // Redirige al usuario a la URL filtrada
  window.location.href = url;
};

// Función para marcar el radio button de la categoría basado en los parámetros de la URL y filtrar productos al cargar la página
const setCategoryInURL = () => {
  // Obtiene los parámetros de la URL
  const paramsURL = new URLSearchParams(window.location.search);

  // Obtiene el valor de categoría seleccionada de los parámetros de la URL
  const categorySelected = paramsURL.get("categoryId");

  // Primera forma:

  // Obtiene todos los radio buttons de categoría
  // const otherRadios = document.querySelectorAll('input[name="category"]');

  // Obtiene el radio button de "Todas las categorias seleccionadas" por su ID
  // const rbtodos = document.getElementById("0");

  // Verifica si alguna otra categoría está marcada al cargar la página
  /*
  otherRadios.forEach((radio) => {
    if (radio.checked) {
      categorySelected = true;
    }
  });
  */

  // Marca el radio button "Todos" si no se selecciona ninguna otra categoría
  /*
  if (!categorySelected) {
    rbtodos.checked = true;
  }
  */

  // Filtra productos al cargar la página
  /*
  if (categorySelected) {
    const radioButtonsCategoria = document.querySelectorAll(
      'input[name="category"]'
    );
    radioButtonsCategoria.forEach((radio) => {
      if (radio.value === categorySelected) {
        radio.checked = true;
      }
    });
  }
  */

  // Segunda forma
  // Obtiene todos los radio buttons de categoría
  const allRadios = document.querySelectorAll('input[name="category"]');

  // Recorre cada radio button de categoría
  allRadios.forEach((radio) => {
    // Si el valor del radio button coincide con la categoría seleccionada en la URL,
    // marcarlo como seleccionado
    if (radio.value === categorySelected) {
      radio.checked = true;
    }
    // Si no hay ninguna categoría seleccionada o el valor es "0" (todas las categorías),
    // marcar el radio button con ID "0"
    else if (!categorySelected || categorySelected === "0") {
      document.getElementById("0").checked = true;
    }
  });
};

// Llamar a la función cuando la página se cargue
window.addEventListener("load", setCategoryInURL);
