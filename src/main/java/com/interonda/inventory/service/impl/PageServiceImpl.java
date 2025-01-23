package com.interonda.inventory.service.impl;

import com.interonda.inventory.entity.PageDetails;
import com.interonda.inventory.service.PageService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class PageServiceImpl implements PageService {

    @Override
    public PageDetails getPageDetails(Page<?> page) {
        PageDetails pageDetails = new PageDetails();
        pageDetails.setNumber(page.getNumber());
        pageDetails.setSize(page.getSize());
        pageDetails.setTotalPages(page.getTotalPages());
        pageDetails.setHasPrevious(page.hasPrevious());
        pageDetails.setHasNext(page.hasNext());
        pageDetails.setPreviousPageUrl("/main?page=" + (page.getNumber() - 1));
        pageDetails.setNextPageUrl("/main?page=" + (page.getNumber() + 1));
        pageDetails.setPageUrl(i -> "/main?page=" + i);
        return pageDetails;
    }
}
