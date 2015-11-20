package org.cloudfoundry.identity.uaa.config;

import org.cloudfoundry.identity.uaa.test.JdbcTestBase;
import org.cloudfoundry.identity.uaa.zone.IdentityZone;
import org.cloudfoundry.identity.uaa.zone.IdentityZoneConfiguration;
import org.cloudfoundry.identity.uaa.zone.IdentityZoneProvisioning;
import org.cloudfoundry.identity.uaa.zone.JdbcIdentityZoneProvisioning;
import org.cloudfoundry.identity.uaa.zone.TokenPolicy;
import org.junit.Assert;
import org.junit.Test;

/*******************************************************************************
 * Cloud Foundry
 * Copyright (c) [2009-2015] Pivotal Software, Inc. All Rights Reserved.
 * <p>
 * This product is licensed to you under the Apache License, Version 2.0 (the "License").
 * You may not use this product except in compliance with the License.
 * <p>
 * This product includes a number of subcomponents with
 * separate copyright notices and license terms. Your use of these
 * subcomponents is subject to the terms and conditions of the
 * subcomponent's license, as noted in the LICENSE file.
 *******************************************************************************/
public class IdentityZoneConfigurationBootstrapTests extends JdbcTestBase {


    @Test
    public void tokenPolicy_configured_fromValuesInYaml() throws Exception {
        IdentityZoneProvisioning provisioning = new JdbcIdentityZoneProvisioning(jdbcTemplate);
        IdentityZoneConfigurationBootstrap bootstrap = new IdentityZoneConfigurationBootstrap(provisioning);
        TokenPolicy tokenPolicy = new TokenPolicy();
        tokenPolicy.setAccessTokenValidity(3600);
        bootstrap.setTokenPolicy(tokenPolicy);
        bootstrap.afterPropertiesSet();

        IdentityZone zone = provisioning.retrieve(IdentityZone.getUaa().getId());
        IdentityZoneConfiguration definition = zone.getConfig();
        Assert.assertEquals(3600, definition.getTokenPolicy().getAccessTokenValidity());
    }
}
