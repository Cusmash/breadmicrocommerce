package com.bread.productservice.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagedResponseDTO<T> {

    private List<T> content; // listItems current page
    private int pageNumber; // current page
    private int pageSize; // items per page
    private long totalElements; // totalItems all pages
    private int totalPages; // number of pages
    private boolean last; // isLastPage
}
