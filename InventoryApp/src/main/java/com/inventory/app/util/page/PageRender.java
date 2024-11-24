package com.inventory.app.util.page;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

// Clase para renderizar la paginación en la aplicación
public class PageRender<T> {

    // Url base para los enlaces de paginación
    private String baseUrl;

    // Pagina de datos actual
    private Page<T> currentPage;

    // Numero total de pageItems
    private int totalPages;

    // Numero de botones por pagina
    private int elementsPerPage;

    // Numero de la pagina actual
    private int currentPageNumber;

    // Lista de items de la pagina
    private List<PageItem> pageItems;

    // Constructor publico
    public PageRender(String baseUrl, Page<T> currentPage) {
        this.baseUrl = baseUrl;
        this.currentPage = currentPage;
        this.pageItems = new ArrayList<PageItem>();

        // ALGORITMO DE PAGINACION
        // | Primera | Anterior | 1 | 2 | 3 | 4 | 5 | Siguiente | Ultima |

        // Define el numero de elementos por pagina
        elementsPerPage = 5;

        // Calcula el total de pageItems
        totalPages = currentPage.getTotalPages();

        // Determina la pagina actual (comienza startPage 1)
        currentPageNumber = currentPage.getNumber() + 1;

        // Inicio y fin del rango de páginas a mostrar
        int startPage;
        int endPage;

        // Establece el inicio y el fin del rango basado en el numero total de pageItems
        // y botones por pagina para mostrar
        if (totalPages <= elementsPerPage) {
            startPage = 1;
            endPage = totalPages;
        } else {
            if (currentPageNumber <= elementsPerPage / 2) {
                startPage = 1;
                endPage = elementsPerPage;
            } else if (currentPageNumber >= totalPages - elementsPerPage / 2) {
                startPage = totalPages - elementsPerPage + 1;
                endPage = elementsPerPage;
            } else {
                startPage = currentPageNumber - elementsPerPage / 2;
                endPage = elementsPerPage;
            }
        }

        // Crea los elementos de la pagina basado en los rangos
        for (int i = 0; i < endPage; i++) {
            pageItems.add(new PageItem(startPage + i, currentPageNumber == startPage + i));
        }

    }

    // Indica si la página actual es la primera
    public boolean isFirst() {
        return currentPage.isFirst();
    }

    // Indica si la página actual es la última
    public boolean isLast() {
        return currentPage.isLast();
    }

    // Indica si hay una siguiente página
    public boolean isHasNext() {
        return currentPage.hasNext();
    }

    // Indica si hay una página anterior
    public boolean isHasPrevious() {
        return currentPage.hasPrevious();
    }

}
