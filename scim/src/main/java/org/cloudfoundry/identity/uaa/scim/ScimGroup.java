/*******************************************************************************
 *     Cloud Foundry
 *     Copyright (c) [2009-2014] Pivotal Software, Inc. All Rights Reserved.
 *
 *     This product is licensed to you under the Apache License, Version 2.0 (the "License").
 *     You may not use this product except in compliance with the License.
 *
 *     This product includes a number of subcomponents with
 *     separate copyright notices and license terms. Your use of these
 *     subcomponents is subject to the terms and conditions of the
 *     subcomponent's license, as noted in the LICENSE file.
 *******************************************************************************/
package org.cloudfoundry.identity.uaa.scim;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;


@JsonSerialize(using = ScimGroupJsonSerializer.class, include = JsonSerialize.Inclusion.NON_NULL)
@JsonDeserialize(using = ScimGroupJsonDeserializer.class)
public class ScimGroup extends ScimCore {

    private String displayName;
    private String zoneId;

    private List<ScimGroupMember> members;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public List<ScimGroupMember> getMembers() {
        return members;
    }

    public void setMembers(List<ScimGroupMember> members) {
        this.members = members;
    }

    public ScimGroup() {
        this(null);
    }

    public ScimGroup(String name) {
        this(null,name,null);
    }

    public ScimGroup(String id, String name, String zoneId) {
        super(id);
        this.displayName = name;
        this.zoneId = zoneId;
    }

    @Override
    public String toString() {
        return String.format("(Group id: %s, name: %s, created: %s, modified: %s, version: %s, members: %s)", getId(),
                        displayName, getMeta().getCreated(), getMeta().getLastModified(), getVersion(), members);
    }
}
