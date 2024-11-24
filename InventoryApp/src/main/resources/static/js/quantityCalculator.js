// Obtiene los siguientes elementos de la página web
// Campo de cantidad de producto
let quantity = document.getElementById("txtQuantity")

// Campo de la nueva cantidad que se va a agregar o disminuirr
let quantityCalculator = document.getElementById("txtQuantityCalculator")

// Campos de tipo radio para establecer la operación
let addQuantity = document.getElementById("rdoAddQuantity")
let substractQuantity = document.getElementById("rdoSubstractQuantity")

// Asigna en una variable la cantidad original del producto
// La función global parseInt sirve para conviertir un String a un número entero, en este y los demás casos son en base 10 (por defecto)
let originalQuantity = parseInt(quantity.value, 10)

// Función para modificar la cantidad original
const editQuantity = () => {

    // Solamente, luego de cargar la pagina, si el campo para calcular la cantidad esta vacio
    if (quantityCalculator.value === ""){
        // Se mantendrá la cantidaa original en el campo quantity
        // El usuario podra modificar la cantidad original del producto
        quantity.value = originalQuantity;
        quantity.disabled = false

    } else {
        // De lo contrario, si la nueva cantidad es diferente de 0
        if(parseInt(quantityCalculator.value, 10) !== 0){
            // Inhabilitara el campo para modificar la cantidad original
            quantity.disabled = true

            if (addQuantity.checked){
                // Si la operación es sumar
                // Agregara y establecera la nueva cantidad a la cantidad original
                quantity.value = originalQuantity + parseInt(quantityCalculator.value, 10)
            } else if (substractQuantity.checked){
                // Si la operación es restar
                // Disminuira y establecera la nueva cantidad a la cantidad original
                quantity.value = originalQuantity - parseInt(quantityCalculator.value, 10)

                // Caso especial, si la nueva cantidad es menor que la cantidad original
                if (parseInt(quantityCalculator.value, 10) > originalQuantity){
                    // Para que no ocurra un error, hara lo siguiente:
                    // Establecera la cantidad original del producto
                    // Asignara el valor 0 en la nueva cantidad
                    quantity.value = originalQuantity
                    quantityCalculator.value = 0;
                }
            }    
        } else {
            // De lo contrario, si la nueva cantidad es 0
            // El usuario podra modificar la cantidad original del producto
            quantity.disabled = false
        }
    }
}

// Agrega los eventos correspondientes a los campos del formulario
quantityCalculator.addEventListener("input", editQuantity);
addQuantity.addEventListener("input", editQuantity);
substractQuantity.addEventListener("input", editQuantity);

// Llama a la función
editQuantity()

