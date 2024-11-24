package com.inventory.app.util.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

// Representa un elemento individual dentro de una paginación
public class PageItem {

    // Numero de página
    private int pageNumber;

    // Indica si este elemento es la página actual
    private boolean isCurrentPage;

}