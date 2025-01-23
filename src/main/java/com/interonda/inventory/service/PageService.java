package com.interonda.inventory.service;

import com.interonda.inventory.entity.PageDetails;
import org.springframework.data.domain.Page;

public interface PageService {

    public PageDetails getPageDetails(Page<?> page);

}