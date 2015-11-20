/*******************************************************************************
 *     Cloud Foundry
 *     Copyright (c) [2009-2015] Pivotal Software, Inc. All Rights Reserved.
 *
 *     This product is licensed to you under the Apache License, Version 2.0 (the "License").
 *     You may not use this product except in compliance with the License.
 *
 *     This product includes a number of subcomponents with
 *     separate copyright notices and license terms. Your use of these
 *     subcomponents is subject to the terms and conditions of the
 *     subcomponent's license, as noted in the LICENSE file.
 *******************************************************************************/
package org.cloudfoundry.identity.uaa.provider;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SamlIdentityProviderDefinition extends ExternalIdentityProviderDefinition {

    public static final String DEFAULT_HTTP_SOCKET_FACTORY = "org.apache.commons.httpclient.protocol.DefaultProtocolSocketFactory";
    public static final String DEFAULT_HTTPS_SOCKET_FACTORY = "org.apache.commons.httpclient.contrib.ssl.EasySSLProtocolSocketFactory";

    public enum MetadataLocation {
        URL,
        DATA,
        UNKNOWN
    };

    private String metaDataLocation;
    private String idpEntityAlias;
    private String zoneId;
    private String nameID;
    private int assertionConsumerIndex;
    private boolean metadataTrustCheck;
    private boolean showSamlLink;
    private String socketFactoryClassName;
    private String linkText;
    private String iconUrl;
    private boolean addShadowUserOnLogin = true;

    public SamlIdentityProviderDefinition clone() {
        return new SamlIdentityProviderDefinition(metaDataLocation,
                                                  idpEntityAlias,
                                                  nameID,
                                                  assertionConsumerIndex,
                                                  metadataTrustCheck,
                                                  showSamlLink,
                                                  linkText,
                                                  iconUrl,
                                                  zoneId,
                                                  addShadowUserOnLogin,
                                                  getEmailDomain() != null ? new ArrayList<>(getEmailDomain()) : null,
                                                  getExternalGroupsWhitelist() != null ? new ArrayList<>(getExternalGroupsWhitelist()) : null,
                                                  getAttributeMappings() != null ? new HashMap(getAttributeMappings()) : null);
    }

    public SamlIdentityProviderDefinition() {}

    public SamlIdentityProviderDefinition(String metaDataLocation,
                                          String idpEntityAlias,
                                          String nameID,
                                          int assertionConsumerIndex,
                                          boolean metadataTrustCheck,
                                          boolean showSamlLink,
                                          String linkText,
                                          String iconUrl,
                                          String zoneId) {
        this.metaDataLocation = metaDataLocation;
        this.idpEntityAlias = idpEntityAlias;
        this.nameID = nameID;
        this.assertionConsumerIndex = assertionConsumerIndex;
        this.metadataTrustCheck = metadataTrustCheck;
        this.showSamlLink = showSamlLink;
        this.linkText = linkText;
        this.iconUrl = iconUrl;
        this.zoneId = zoneId;
    }

    public SamlIdentityProviderDefinition(String metaDataLocation,
                                          String idpEntityAlias,
                                          String nameID,
                                          int assertionConsumerIndex,
                                          boolean metadataTrustCheck,
                                          boolean showSamlLink,
                                          String linkText,
                                          String iconUrl,
                                          String zoneId,
                                          boolean addShadowUserOnLogin,
                                          List<String> emailDomain,
                                          List<String> externalGroupsWhitelist,
                                          Map<String, Object> attributeMappings) {
        this.metaDataLocation = metaDataLocation;
        this.idpEntityAlias = idpEntityAlias;
        this.nameID = nameID;
        this.assertionConsumerIndex = assertionConsumerIndex;
        this.metadataTrustCheck = metadataTrustCheck;
        this.showSamlLink = showSamlLink;
        this.linkText = linkText;
        this.iconUrl = iconUrl;
        this.zoneId = zoneId;
        this.addShadowUserOnLogin = addShadowUserOnLogin;
        setEmailDomain(emailDomain);
        setExternalGroupsWhitelist(externalGroupsWhitelist);
        setAttributeMappings(attributeMappings);
    }

    @JsonIgnore
    public MetadataLocation getType() {
        String trimmedLocation = metaDataLocation.trim();
        if (trimmedLocation.startsWith("<?xml") ||
            trimmedLocation.startsWith("<md:EntityDescriptor") ||
            trimmedLocation.startsWith("<EntityDescriptor")) {
            if(validateXml(trimmedLocation)) {
                return MetadataLocation.DATA;
            }
        } else if (trimmedLocation.startsWith("http")) {
            try {
                URL uri = new URL(trimmedLocation);
                return MetadataLocation.URL;
            } catch (MalformedURLException e) {
                //invalid URL
            }
        }
        return MetadataLocation.UNKNOWN;
    }

    private boolean validateXml(String xml) {
        if (xml==null || xml.toUpperCase().contains("<!DOCTYPE")) {
            return false;
        }
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.parse(new InputSource(new StringReader(xml)));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            return false;
        }

        return true;
    }

    public String getMetaDataLocation() {
        return metaDataLocation;
    }

    public void setMetaDataLocation(String metaDataLocation) {
        this.metaDataLocation = metaDataLocation;
    }

    public String getIdpEntityAlias() {
        return idpEntityAlias;
    }

    public void setIdpEntityAlias(String idpEntityAlias) {
        this.idpEntityAlias = idpEntityAlias;
    }

    public String getNameID() {
        return nameID;
    }

    public void setNameID(String nameID) {
        this.nameID = nameID;
    }

    public int getAssertionConsumerIndex() {
        return assertionConsumerIndex;
    }

    public void setAssertionConsumerIndex(int assertionConsumerIndex) {
        this.assertionConsumerIndex = assertionConsumerIndex;
    }

    public boolean isMetadataTrustCheck() {
        return metadataTrustCheck;
    }

    public void setMetadataTrustCheck(boolean metadataTrustCheck) {
        this.metadataTrustCheck = metadataTrustCheck;
    }

    public boolean isShowSamlLink() {
        return showSamlLink;
    }

    public void setShowSamlLink(boolean showSamlLink) {
        this.showSamlLink = showSamlLink;
    }

    public String getSocketFactoryClassName() {
        if (socketFactoryClassName!=null && socketFactoryClassName.trim().length()>0) {
            return socketFactoryClassName;
        }
        if (getMetaDataLocation()==null || getMetaDataLocation().trim().length()==0) {
            throw new IllegalStateException("Invalid meta data URL[" + getMetaDataLocation() + "] cannot determine socket factory.");
        }
        if (getMetaDataLocation().startsWith("https")) {
            return DEFAULT_HTTPS_SOCKET_FACTORY;
        } else {
            return DEFAULT_HTTP_SOCKET_FACTORY;
        }
    }

    public void setSocketFactoryClassName(String socketFactoryClassName) {
        this.socketFactoryClassName = socketFactoryClassName;
        if (socketFactoryClassName!=null && socketFactoryClassName.trim().length()>0) {
            try {
                Class.forName(
                    socketFactoryClassName,
                    true,
                    Thread.currentThread().getContextClassLoader()
                );
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException(e);
            } catch (ClassCastException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    public String getLinkText() {
        return linkText;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public boolean isAddShadowUserOnLogin() {
        return addShadowUserOnLogin;
    }

    public void setAddShadowUserOnLogin(boolean addShadowUserOnLogin) {
        this.addShadowUserOnLogin = addShadowUserOnLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SamlIdentityProviderDefinition that = (SamlIdentityProviderDefinition) o;

        return Objects.equals(getUniqueAlias(), that.getUniqueAlias());
    }

    @Override
    public int hashCode() {
        String alias = getUniqueAlias();
        return alias==null ? 0 : alias.hashCode();
    }

    @JsonIgnore
    public String getUniqueAlias() {
        return getIdpEntityAlias()+"###"+getZoneId();
    }

    @Override
    public String toString() {
        return "SamlIdentityProviderDefinition{" +
            "idpEntityAlias='" + idpEntityAlias + '\'' +
            ", metaDataLocation='" + metaDataLocation + '\'' +
            ", nameID='" + nameID + '\'' +
            ", assertionConsumerIndex=" + assertionConsumerIndex +
            ", metadataTrustCheck=" + metadataTrustCheck +
            ", showSamlLink=" + showSamlLink +
            ", socketFactoryClassName='" + socketFactoryClassName + '\'' +
            ", linkText='" + linkText + '\'' +
            ", iconUrl='" + iconUrl + '\'' +
            ", zoneId='" + zoneId + '\'' +
            ", addShadowUserOnLogin='" + addShadowUserOnLogin + '\'' +
            '}';
    }
}
