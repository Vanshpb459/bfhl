package com.assignment.bfhl.service;

import com.assignment.bfhl.dto.BfhlRequest;
import com.assignment.bfhl.dto.BfhlResponse;

public interface BfhlService {

    /**
     * Processes the incoming data list and returns a categorised response.
     *
     * @param request the incoming request containing a mixed list of strings
     * @return a fully populated {@link BfhlResponse}
     */
    BfhlResponse processData(BfhlRequest request);
}
