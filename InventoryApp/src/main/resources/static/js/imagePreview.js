// Obtiene el elemento del documento HTML por su ID
const btnFileImage = document.getElementById("btnFileImage");
const previewImage = document.getElementById("previewImage");

// Función para mostrar la imagen
const showImage = (source) => {
  // Establece la fuente de la imagen al valor proporcionado
  previewImage.src = source;
  // Asegura de que la imagen esté visible con el estilo display: "block"
  previewImage.style.display = "block";
};

// Función para eliminar la imagen de la previsualización
const clearImagePreview = () => {
  // Limpia la fuente de la imagen
  previewImage.src = "";
  // Oculta la imagen cuando no haya una previsualización
  previewImage.style.display = "none";
};

// Función para cargar la imagen seleccionada
const loadImage = () => {
  // Primero elimina cualquier imagen previamente cargada
  clearImagePreview();

  // Verifica si hay archivos seleccionados y si hay al menos un archivo
  if (btnFileImage.files && btnFileImage.files[0]) {
    // Crea una nueva instancia de FileReader
    // El objeto FileReader permite que las aplicaciones web lean ficheros (o información en buffer) almacenados en el
    // cliente de forma asíncrona, usando los objetos File o Blob dependiendo de los datos que se pretenden leer.
    const reader = new FileReader();

    // Define lo que sucede una vez que el archivo es leído
    // Este evento se activa cada vez que la operación de lectura se ha completado satisfactoriamente.
    reader.onload = function (event) {
      // Llama a showImage con el resultado leído
      showImage(event.target.result);
    };

    // Lee el archivo como una URL de datos
    // Comienza la lectura del contenido del objeto Blob, una vez terminada, el atributo result contiene un data: URL
    // que representa los datos del fichero.
    reader.readAsDataURL(btnFileImage.files[0]);
  }
};

// Añade un evento 'change' al input de archivo para cargar la imagen seleccionada
// El evento change se dispara para elementos <input>, <select>, y <textarea> cuando una alteración al valor de
// un elemento es confirmada por el usuario.
btnFileImage.addEventListener("change", loadImage);
