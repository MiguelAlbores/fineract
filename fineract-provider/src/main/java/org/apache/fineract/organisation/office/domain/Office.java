/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.fineract.organisation.office.domain;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.StringUtils;
import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.domain.AbstractPersistableCustom;
import org.apache.fineract.organisation.office.exception.CannotUpdateOfficeWithParentOfficeSameAsSelf;
import org.apache.fineract.organisation.office.exception.RootOfficeParentCannotBeUpdated;
import org.joda.time.LocalDate;

@Entity
@Table(name = "m_office", uniqueConstraints = { @UniqueConstraint(columnNames = { "name" }, name = "name_org"),
        @UniqueConstraint(columnNames = { "external_id" }, name = "externalid_org") })
public class Office extends AbstractPersistableCustom<Long> {

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private List<Office> children = new LinkedList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Office parent;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "hierarchy", nullable = true, length = 50)
    private String hierarchy;

    @Column(name = "opening_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date openingDate;

    @Column(name = "external_id", length = 100)
    private String externalId;

    @Column(name = "office_type_id")
    private Long officeTypeId;

    @Column(name = "address", length = 1000)
    private String address;

    @Column(name = "colony", length = 100)
    private String colony;

    @Column(name = "postal_code", length = 100)
    private String postalCode;

    @Column(name = "phone", length = 100)
    private String phone;

    @Column(name = "municipality", length = 255)
    private String municipality;

    @Column(name = "state", length = 255)
    private String state;

    @Column(name = "une")
    private String une;

    public static Office headOffice(final String name, final LocalDate openingDate, final String externalId) {
        return new Office(null, name, openingDate, externalId);
    }

    public static Office fromJson(final Office parentOffice, final JsonCommand command) {

        final String name = command.stringValueOfParameterNamed("name");
        final LocalDate openingDate = command.localDateValueOfParameterNamed("openingDate");
        final String externalId = command.stringValueOfParameterNamed("externalId");
        final Long officeTypeId = command.longValueOfParameterNamed("officeTypeId");
        final String address = command.stringValueOfParameterNamed("address");
        final String colony = command.stringValueOfParameterNamed("colony");
        final String postalCode = command.stringValueOfParameterNamed("postalCode");
        final String phone = command.stringValueOfParameterNamed("phone");
        final String municipality = command.stringValueOfParameterNamed("municipality");
        final String state = command.stringValueOfParameterNamed("state");
        final String une = command.stringValueOfParameterNamed("une");
        return new Office(parentOffice, name, openingDate, externalId, officeTypeId, address, colony,
                postalCode, phone, municipality, state, une);
    }

    protected Office() {
        this.openingDate = null;
        this.parent = null;
        this.name = null;
        this.externalId = null;
    }

    public Office(Office parent, String name, final LocalDate openingDate, String externalId) {
        this.parent = parent;
        this.openingDate = openingDate.toDateTimeAtStartOfDay().toDate();
        if (parent != null) {
            this.parent.addChild(this);
        }

        if (StringUtils.isNotBlank(name)) {
            this.name = name.trim();
        } else {
            this.name = null;
        }
        if (StringUtils.isNotBlank(externalId)) {
            this.externalId = externalId.trim();
        } else {
            this.externalId = null;
        }
    }

    public Office(Office parent, String name, final LocalDate openingDate,
                  String externalId, Long officeTypeId, String address, String colony,
                  String postalCode, String phone, String municipality, String state,
                  String une) {
        this.parent = parent;
        this.openingDate = openingDate.toDateTimeAtStartOfDay().toDate();
        if (parent != null) {
            this.parent.addChild(this);
        }

        if (StringUtils.isNotBlank(name)) {
            this.name = name.trim();
        } else {
            this.name = null;
        }
        if (StringUtils.isNotBlank(externalId)) {
            this.externalId = externalId.trim();
        } else {
            this.externalId = null;
        }

        this.officeTypeId = officeTypeId;
        this.address = address;
        this.colony = colony;
        this.postalCode = postalCode;
        this.phone = phone;
        this.municipality = municipality;
        this.state = state;
        this.une = une;
    }

    private void addChild(final Office office) {
        this.children.add(office);
    }

    public Map<String, Object> update(final JsonCommand command) {

        final Map<String, Object> actualChanges = new LinkedHashMap<>(7);

        final String dateFormatAsInput = command.dateFormat();
        final String localeAsInput = command.locale();

        final String parentIdParamName = "parentId";

        if (command.parameterExists(parentIdParamName) && this.parent == null) { throw new RootOfficeParentCannotBeUpdated(); }

        if (this.parent != null && command.isChangeInLongParameterNamed(parentIdParamName, this.parent.getId())) {
            final Long newValue = command.longValueOfParameterNamed(parentIdParamName);
            actualChanges.put(parentIdParamName, newValue);
        }

        final String openingDateParamName = "openingDate";
        if (command.isChangeInLocalDateParameterNamed(openingDateParamName, getOpeningLocalDate())) {
            final String valueAsInput = command.stringValueOfParameterNamed(openingDateParamName);
            actualChanges.put(openingDateParamName, valueAsInput);
            actualChanges.put("dateFormat", dateFormatAsInput);
            actualChanges.put("locale", localeAsInput);

            final LocalDate newValue = command.localDateValueOfParameterNamed(openingDateParamName);
            this.openingDate = newValue.toDate();
        }

        final String nameParamName = "name";
        if (command.isChangeInStringParameterNamed(nameParamName, this.name)) {
            final String newValue = command.stringValueOfParameterNamed(nameParamName);
            actualChanges.put(nameParamName, newValue);
            this.name = newValue;
        }

        final String externalIdParamName = "externalId";
        if (command.isChangeInStringParameterNamed(externalIdParamName, this.externalId)) {
            final String newValue = command.stringValueOfParameterNamed(externalIdParamName);
            actualChanges.put(externalIdParamName, newValue);
            this.externalId = StringUtils.defaultIfEmpty(newValue, null);
        }

        final String officeTypeParamName = "officeTypeId";
        if(command.isChangeInLongParameterNamed(officeTypeParamName, this.officeTypeId)){
            final Long newValue = command.longValueOfParameterNamed(officeTypeParamName);
            actualChanges.put(officeTypeParamName, newValue);
            this.officeTypeId = newValue;
        }

        final String addressParamName = "address";
        if (command.isChangeInStringParameterNamed(externalIdParamName, this.address)) {
            final String newValue = command.stringValueOfParameterNamed(addressParamName);
            actualChanges.put(addressParamName, newValue);
            this.address = StringUtils.defaultIfEmpty(newValue, null);
        }

        final String colonyParamName = "colony";
        if (command.isChangeInStringParameterNamed(colonyParamName, this.colony)) {
            final String newValue = command.stringValueOfParameterNamed(colonyParamName);
            actualChanges.put(colonyParamName, newValue);
            this.colony = StringUtils.defaultIfEmpty(newValue, null);
        }

        final String postalCodeParamName = "postalCode";
        if (command.isChangeInStringParameterNamed(postalCodeParamName, this.postalCode)) {
            final String newValue = command.stringValueOfParameterNamed(postalCodeParamName);
            actualChanges.put(postalCodeParamName, newValue);
            this.postalCode = StringUtils.defaultIfEmpty(newValue, null);
        }

        final String phoneParamName = "phone";
        if (command.isChangeInStringParameterNamed(phoneParamName, this.phone)) {
            final String newValue = command.stringValueOfParameterNamed(phoneParamName);
            actualChanges.put(phoneParamName, newValue);
            this.phone = StringUtils.defaultIfEmpty(newValue, null);
        }

        final String municipalityParamName = "municipality";
        if (command.isChangeInStringParameterNamed(municipalityParamName, this.municipality)) {
            final String newValue = command.stringValueOfParameterNamed(municipalityParamName);
            actualChanges.put(municipalityParamName, newValue);
            this.municipality = StringUtils.defaultIfEmpty(newValue, null);
        }

        final String stateParamName = "state";
        if (command.isChangeInStringParameterNamed(stateParamName, this.state)) {
            final String newValue = command.stringValueOfParameterNamed(stateParamName);
            actualChanges.put(stateParamName, newValue);
            this.state = StringUtils.defaultIfEmpty(newValue, null);
        }

        final String uneParamName = "une";
        if (command.isChangeInStringParameterNamed(uneParamName, this.une)) {
            final String newValue = command.stringValueOfParameterNamed(uneParamName);
            actualChanges.put(uneParamName, newValue);
            this.une = StringUtils.defaultIfEmpty(newValue, null);
        }

        return actualChanges;
    }

    public boolean isOpeningDateBefore(final LocalDate baseDate) {
        return getOpeningLocalDate().isBefore(baseDate);
    }

    public boolean isOpeningDateAfter(final LocalDate activationLocalDate) {
        return getOpeningLocalDate().isAfter(activationLocalDate);
    }

    public LocalDate getOpeningLocalDate() {
        LocalDate openingLocalDate = null;
        if (this.openingDate != null) {
            openingLocalDate = LocalDate.fromDateFields(this.openingDate);
        }
        return openingLocalDate;
    }

    public void update(final Office newParent) {

        if (this.parent == null) { throw new RootOfficeParentCannotBeUpdated(); }

        if (identifiedBy(newParent.getId())) { throw new CannotUpdateOfficeWithParentOfficeSameAsSelf(getId(), newParent.getId()); }

        this.parent = newParent;
        generateHierarchy();
    }

    public boolean identifiedBy(final Long id) {
        return getId().equals(id);
    }

    public void generateHierarchy() {

        if (this.parent != null) {
            this.hierarchy = this.parent.hierarchyOf(getId());
        } else {
            this.hierarchy = ".";
        }
    }

    private String hierarchyOf(final Long id) {
        return this.hierarchy + id.toString() + ".";
    }

    public String getName() {
        return this.name;
    }

    public String getHierarchy() {
        return this.hierarchy;
    }

    public Office getParent() {
    	return this.parent;
    }
    
    public boolean hasParentOf(final Office office) {
        boolean isParent = false;
        if (this.parent != null) {
            isParent = this.parent.equals(office);
        }
        return isParent;
    }

    public boolean doesNotHaveAnOfficeInHierarchyWithId(final Long officeId) {
        return !hasAnOfficeInHierarchyWithId(officeId);
    }

    private boolean hasAnOfficeInHierarchyWithId(final Long officeId) {

        boolean match = false;

        if (identifiedBy(officeId)) {
            match = true;
        }

        if (!match) {
            for (final Office child : this.children) {
                final boolean result = child.hasAnOfficeInHierarchyWithId(officeId);

                if (result) {
                    match = result;
                    break;
                }
            }
        }

        return match;
    }
    
    public void loadLazyCollections() {
        this.children.size() ;
    }

    public Long getOfficeTypeId() {
        return officeTypeId;
    }
}