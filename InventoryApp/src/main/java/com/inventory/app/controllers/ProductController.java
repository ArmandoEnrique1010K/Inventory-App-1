package com.inventory.app.controllers;

import com.inventory.app.models.dto.CategoryDto;
import com.inventory.app.models.dto.ProductDto;
import com.inventory.app.models.entities.Product;
import com.inventory.app.models.requests.ProductRequest;
import com.inventory.app.services.CategoryService;
import com.inventory.app.services.ProductService;
import com.inventory.app.util.page.PageRender;

import jakarta.validation.Valid;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

// Controlador para manejar las rutas relacionadas con products
// El controlador hereda la clase CategoryExtends
@Controller
@RequestMapping("/products")
public class ProductController extends CategoryExtends {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    // Pagina dinamica para obtener todos los productos
    // page representa el numero de pagina a visualizar, por defecto es 0
    @GetMapping
    public String dynamicPageListAllProducts(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

        // Crea un PageRequest con el número de página y el número de elementos por
        // página (10)
        Pageable pageRequest = PageRequest.of(page, 10);

        // Obtiene una página de productos utilizando el servicio de productos
        Page<ProductDto> products = productService.findAll(pageRequest);

        // Crea un PageRender para manejar la paginación en la vista
        PageRender<ProductDto> pageRender = new PageRender<>("/products", products);

        // Si el numero de pagina actual es menor que el total de paginas
        // currentPageNumber empieza por 0 (pagina 1)
        // if (pageRender.getCurrentPageNumber() > pageRender.getTotalPages()) {
        // Devuelve una pagina de error 204
        // return "error204";
        // }

        // Añade un texto vacío para mostrar en la barra de búsqueda
        model.addAttribute("keyword", "");

        // Añade la lista de productos al modelo
        model.addAttribute("products", products);

        // Añade el objeto PageRender al modelo para manejar la paginación
        model.addAttribute("page", pageRender);

        // Llama al método para generar las categorías y añadirlas al modelo
        generateCategories(model);

        // Devuelve el nombre de la vista "listProducts"
        return "listProducts";

    }

    // Pagina dinamica para obtener los productos segun el id de categoria
    // categoryId representa el parametro del id de la categoria
    @GetMapping("/category/{categoryId}")
    public String dynamicPageListProductsByCategory(@RequestParam(name = "page", defaultValue = "0") int page,
            @PathVariable Long categoryId,
            Model model) {

        Pageable pageRequest = PageRequest.of(page, 10);

        Page<ProductDto> products = productService.findAllByCategoryId(
                pageRequest, categoryId);

        PageRender<ProductDto> pageRender = new PageRender<>("/products", products);

        // Llama al servicio para buscar la categoria por su Id
        Optional<CategoryDto> categorybyId = categoryService.findById(categoryId);

        // Obtiene el nombre de la categoria encontrada
        String nameCategory = categorybyId.get().getName();

        // Obtiene el estado de la categoria encontrada
        Boolean statusCategory = categoryService.getStatusById(categoryId);

        // Si el estado de la categoria es falso, renderiza una pagina de error
        if (statusCategory == false) {
            generateCategories(model);
            return "error204";
        }

        // Si el numero de pagina actual es menor que el total de paginas
        // if (products.hasContent() && pageRender.getCurrentPageNumber() >
        // pageRender.getTotalPages()) {
        // return "error204";
        // }

        model.addAttribute("keyword", "");
        model.addAttribute("products", products);
        model.addAttribute("page", pageRender);

        // Añade el nombre de la categoria al modelo
        model.addAttribute("nameCategory", nameCategory);

        generateCategories(model);

        return "listProductsByCategory.html";

    }

    // Pagina dinamica para obtener los productos segun el id de categoria y una
    // word representa el parametro de la palabra clave
    @GetMapping("/search")
    public String dynamicPageSearchListProducts(@RequestParam(value = "word", required = false) String word,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model) {

        // Construye los componentes del endpoint para mantener los parámetros en la
        // navegación
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath("/products/search");

        // Añade el parámetro de categoría al endpoint si está presente
        if (categoryId != null) {
            uriComponentsBuilder.queryParam("categoryId", categoryId);
        }

        // Añade la palabra clave al endpoint si está presente
        if (word != null && !word.isEmpty()) {
            uriComponentsBuilder.queryParam("name", word);
        }

        // Obtiene la cadena de consulta completa con los parámetros
        String urlParams = uriComponentsBuilder.toUriString();
        model.addAttribute("urlParams", urlParams);

        // Crea un PageRequest con el número de página y el número de elementos por
        // página (10)
        Pageable pageRequest = PageRequest.of(page, 10);

        // Obtiene una página de productos filtrados por categoría y palabra clave
        // utilizando el servicio de productos
        Page<ProductDto> products = productService
                .findAllByCategoryIdAndNameLike(pageRequest,
                        categoryId, word);

        // Crea un PageRender para manejar la paginación en la vista
        PageRender<ProductDto> pageRender = new PageRender<>("/products/search" + categoryId,
                products);

        // Obtiene el estado de la categoria encontrada
        // Boolean statusCategory = categoryService.getStatusById(categoryId);

        // Si no se ha introducido una palabra clave
        if (word == null || word.trim().isEmpty()) {
            // model.addAttribute("keyword", word);
            // model.addAttribute("products",productService.findAll(pageRequest));
            // model.addAttribute("page", pageRender);
            generateCategories(model);

            // Devuelve una vista de error 204
            return "error204";
        }

        // if (statusCategory == false) {
        // generateCategories(model);
        // return "error204";
        // }

        // Almacena la cantidad de productos encontrados segun los parametros
        Long quantityProducts = productService
                .countByCategoryIdAndNameLike(categoryId, word);

        // Cuenta si la cantidad de productos es mayor que 0 y si la pagina actual es
        // mayor que es el total de paginas
        // if (quantityProducts > 0 && pageRender.getCurrentPageNumber() >
        // pageRender.getTotalPages()) {
        // return "error204";
        // }

        // Añade la cantidad de productos encontrados al modelo
        model.addAttribute("quantityProducts", quantityProducts);

        model.addAttribute("products", products);
        model.addAttribute("keyword", word);
        model.addAttribute("page", pageRender);

        generateCategories(model);
        return "searchProducts.html";

    }

    // Pagina para mostrar los detalles de un producto
    @GetMapping("/details/{id}")
    public String getProduct(@PathVariable Long id, Model model) {

        // Obtiene el estado del producto
        var productStatus = productService.getStatusById(id);

        // Si su estado es falso, renderiza una modelo de error 204
        if (productStatus == false) {

            generateCategories(model);
            return "error204";

        } else {

            // De lo contrario, busca el producto por su id
            Optional<ProductDto> productById = productService.findById(id);

            // Añade los valores de sus respectivos campos al modelo
            model.addAttribute("id", productById.orElseThrow().getId());
            model.addAttribute("name", productById.orElseThrow().getName());
            model.addAttribute("entryDate", productById.orElseThrow().getEntryDate());
            model.addAttribute("length", productById.orElseThrow().getLength());
            model.addAttribute("width", productById.orElseThrow().getWidth());
            model.addAttribute("quantity", productById.orElseThrow().getQuantity());
            model.addAttribute("nameImage", productById.orElseThrow().getNameImage());
            model.addAttribute("categoryName", productById.orElseThrow().getCategoryName());
            model.addAttribute("categoryColor", productById.orElseThrow().getCategoryColor());

            generateCategories(model);
            return "oneProduct.html";

        }

    }

    // Formulario para añadir un producto
    @GetMapping("/add")
    public String formAddProduct(Model model) {

        Product product = new Product();

        // Se coloca generateCategories primero, en el caso de que ocurra un error, al
        // enviar el formulario con campos vacios, la categoria quedara seleccionada
        generateCategories(model);

        // Luego añade los campos que fueron rellenados por el usuario
        model.addAttribute("product", product);
        return "formAddProduct.html";

    }

    // Método para agregar un producto
    @PostMapping("/add")
    public String saveProduct(@ModelAttribute("product") @Valid Product product,
            BindingResult result,
            Model model) {

        // Valida que no haya errores en ningun campo
        if (result.hasErrors()) {
            generateCategories(model);
            model.addAttribute("product", product);
            return "formAddProduct.html";
        }

        // Si no hay una imagen subida
        if (product.getFileImage().isEmpty()) {
            generateCategories(model);
            model.addAttribute("product", product);

            // Muestra un mensaje de error y renderiza el formulario de añadir producto
            model.addAttribute("imageError", "Suba una imagen");
            return "formAddProduct.html";
        }

        // Lista de tipos MIME permitidos
        List<String> allowedMimeTypes = Arrays.asList("image/png", "image/jpeg", "image/jpg", "image/gif");

        // Si archivo subido no es una imagen
        if (!allowedMimeTypes.contains(product.getFileImage().getContentType())) {
            generateCategories(model);
            model.addAttribute("product", product);

            // Realiza el mismo procedimiento
            model.addAttribute("imageError",
                    "El archivo no es una imagen, se admiten los siguientes formatos: .png, .jpeg, .jpg y .gif");
            return "formAddProduct.html";

        }

        // Llama al servicio para guardar el producto
        productService.save(product);

        // No se utiliza esto
        // try {
        // productService.save(product);
        // } catch (Exception e) {
        // generateCategories(model);
        // }

        // Redirecciona a la pagina de todos los productos
        return "redirect:/products";

    }

    // Formulario para editar un producto
    @GetMapping("/edit/{id}")
    public String formEditProduct(@PathVariable Long id, Model model) {

        // Obtiene el estado del producto por su id
        var productStatus = productService.getStatusById(id);

        // Si tiene el estado false, renderiza un modelo de error
        if (productStatus == false) {

            generateCategories(model);
            return "error204";

        } else {

            // De lo contrario, obtiene los datos del producto por su id y lo añade al
            // modelo
            var productData = productService.findById(id).orElseThrow();

            generateCategories(model);
            model.addAttribute("product", productData);
            model.addAttribute("id", productData.getId());

            // Renderiza el formulario de editar producto
            return "formEditProduct.html";

        }

    }

    // Método para editar un producto
    // RequestParam sirve para obtener el contenido desde el modelo y realizar una
    // operación desde el controlador, en este caso se obtiene la imagen que fue
    // subida
    @PostMapping("/edit/{id}")
    public String updateProduct(
            @PathVariable Long id,
            @ModelAttribute("product") @Valid ProductRequest productRequest,
            BindingResult result,
            @RequestParam(name = "fileImage", required = false) MultipartFile fileImage,
            Model model) {

        // Verifica si hay errores de validación
        if (result.hasErrors()) {

            // Obtiene el producto por el id
            var productData = productService.findById(id).orElseThrow();

            generateCategories(model);
            model.addAttribute("product", productRequest);
            model.addAttribute("id", productData.getId());
            return "formEditProduct.html";
        }

        // Recuperar el ProductDto existente
        // ProductDto currentProduct = productService.findById(id)
        // .orElseThrow(() -> new RuntimeException("Product no encontrado"));

        // Obtiene el producto por su id
        ProductDto productById = productService.findById(id).orElseThrow();

        // Lista de tipos MIME permitidos
        List<String> allowedMimeTypes = Arrays.asList("image/png", "image/jpeg", "image/jpg", "image/gif");

        // Si se ha subido una imagen
        if (!fileImage.isEmpty()) {

            // Si no es una imagen
            if (!allowedMimeTypes.contains(fileImage.getContentType())) {

                generateCategories(model);
                productRequest.setFileImage(productById.getFileImage());
                model.addAttribute("product", productRequest);

                // Realiza el mismo procedimiento
                model.addAttribute("imageError",
                        "El archivo no es una imagen, se admiten los siguientes formatos: .png, .jpeg, .jpg y .gif");
                return "formEditProduct.html";
            }

            // Cambia la imagen del producto
            productRequest.setFileImage(fileImage);
        } else {
            // De lo contrario mantiene la imagen original
            productRequest.setFileImage(productById.getFileImage());
        }

        // Llama al servicio para actualizar un producto
        productService.update(id, productRequest);

        return "redirect:/products";

    }

    // Método para borrar un producto
    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {

        // Redundancia
        // if (id > 0) {
        // productService.deleteById(id);
        // }

        productService.deleteById(id);
        return "redirect:/products";

    }

}
