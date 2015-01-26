/*
 * *****************************************************************************
 *      Cloud Foundry
 *      Copyright (c) [2009-2015] Pivotal Software, Inc. All Rights Reserved.
 *      This product is licensed to you under the Apache License, Version 2.0 (the "License").
 *      You may not use this product except in compliance with the License.
 *
 *      This product includes a number of subcomponents with
 *      separate copyright notices and license terms. Your use of these
 *      subcomponents is subject to the terms and conditions of the
 *      subcomponent's license, as noted in the LICENSE file.
 * *****************************************************************************
 */

package org.cloudfoundry.identity.uaa.mock.util;


import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.cloudfoundry.identity.uaa.scim.ScimGroup;
import org.cloudfoundry.identity.uaa.scim.ScimUser;
import org.cloudfoundry.identity.uaa.zone.IdentityZone;
import org.cloudfoundry.identity.uaa.zone.IdentityZoneCreationRequest;
import org.cloudfoundry.identity.uaa.zone.MultitenancyFixture;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

public class MockMvcUtils {

    public static MockMvcUtils utils() {
        return new MockMvcUtils();
    }

    public IdentityZone createZoneUsingWebRequest(MockMvc mockMvc, String accessToken) throws Exception {
        final String zoneId = UUID.randomUUID().toString();
        IdentityZone identityZone = MultitenancyFixture.identityZone(zoneId, zoneId);

        IdentityZoneCreationRequest creationRequest = new IdentityZoneCreationRequest();
        creationRequest.setIdentityZone(identityZone);

        MvcResult result = mockMvc.perform(post("/identity-zones")
            .header("Authorization", "Bearer " + accessToken)
            .contentType(APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(creationRequest)))
            .andExpect(status().isCreated()).andReturn();
        return new ObjectMapper().readValue(result.getResponse().getContentAsByteArray(), IdentityZone.class);
    }

    public ScimUser createUser(MockMvc mockMvc, String accessToken, ScimUser user) throws Exception {
        MvcResult userResult = mockMvc.perform(post("/Users")
            .header("Authorization", "Bearer " + accessToken)
            .contentType(APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsBytes(user)))
            .andExpect(status().isCreated()).andReturn();
        return new ObjectMapper().readValue(userResult.getResponse().getContentAsString(), ScimUser.class);
    }

    public ScimGroup createGroup(MockMvc mockMvc, String accessToken, ScimGroup group) throws Exception {
        return new ObjectMapper().readValue(
            mockMvc.perform(post("/Groups")
            .header("Authorization", "Bearer " + accessToken)
            .contentType(APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsBytes(group)))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsByteArray(),
            ScimGroup.class);
    }
}