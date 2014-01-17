/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.kenyaemr.metadata.sync;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Location;
import org.openmrs.LocationAttribute;
import org.openmrs.LocationAttributeType;
import org.openmrs.module.kenyaemr.metadata.FacilityMetadata;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.metadatadeploy.source.AbstractCsvResourceSource;

import java.io.IOException;

/**
 * Location source from Master Facility CSV resource
 */
public class LocationMflCsvSource extends AbstractCsvResourceSource<Location> {

	private LocationAttributeType codeAttrType, landlineAttrType, mobileAttrType;

	/**
	 * Constructs a new location source
	 * @param csvFile the csv resource path
	 */
	public LocationMflCsvSource(String csvFile) throws IOException {
		super(csvFile, true);

		this.codeAttrType = MetadataUtils.getLocationAttributeType(FacilityMetadata._LocationAttributeType.MASTER_FACILITY_CODE);
		this.landlineAttrType = MetadataUtils.getLocationAttributeType(FacilityMetadata._LocationAttributeType.TELEPHONE_LANDLINE);
		this.mobileAttrType = MetadataUtils.getLocationAttributeType(FacilityMetadata._LocationAttributeType.TELEPHONE_MOBILE);
	}

	/**
	 * @see org.openmrs.module.metadatadeploy.source.AbstractCsvResourceSource#parseLine(String[])
	 */
	@Override
	public Location parseLine(String[] line) {
		String code = line[0];
		String name = line[1];
		String province = line[2];
		String county = line[3];
		String district = line[4];
		String division = line[5];
		String type = line[6];
		String landline = line[15];
		String mobile = line[17];
		String postcode = line[22];

		Location location = new Location();
		location.setName(name);
		location.setDescription(type);

		// Facility address
		location.setAddress5(division);
		location.setAddress6(district);
		location.setCountyDistrict(county);
		location.setStateProvince(province);
		location.setCountry("Kenya");
		location.setPostalCode(postcode);

		if (StringUtils.isNotEmpty(code)) {
			LocationAttribute attr = new LocationAttribute();
			attr.setAttributeType(codeAttrType);
			attr.setValue(code.trim());
			attr.setOwner(location);
			location.addAttribute(attr);
		}

		if (StringUtils.isNotEmpty(landline)) {
			LocationAttribute attr = new LocationAttribute();
			attr.setAttributeType(landlineAttrType);
			attr.setValue(landline.trim());
			attr.setOwner(location);
			location.addAttribute(attr);
		}

		if (StringUtils.isNotEmpty(mobile)) {
			LocationAttribute attr = new LocationAttribute();
			attr.setAttributeType(mobileAttrType);
			attr.setValue(mobile.trim());
			attr.setOwner(location);
			location.addAttribute(attr);
		}

		return location;
	}
}