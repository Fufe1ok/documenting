package com.lab5.resteventhub.web.rest;

import com.lab5.resteventhub.dto.RequestDTO;
import com.lab5.resteventhub.service.SendDataService;
import com.lab5.resteventhub.LogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class URLController {

    private LogService logService;

    public URLController(LogService logService) {
        this.logService = logService;
    }

    @PostMapping("/url")
    public void addNewUrl(@RequestBody RequestDTO dto) {
        SendDataService logService = this.logService.getService(dto.getStrategy());
        try {
            logService.sendAndLog(dto.getUrl());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}