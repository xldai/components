// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.components.salesforce.runtime.dataprep;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.avro.generic.IndexedRecord;
import org.junit.Assert;
import org.junit.Test;
import org.talend.components.api.component.SupportedProduct;
import org.talend.components.api.component.runtime.Reader;
import org.talend.components.api.exception.ComponentException;
import org.talend.components.salesforce.dataprep.SalesforceInputDefinition;
import org.talend.components.salesforce.dataprep.SalesforceInputProperties;
import org.talend.components.salesforce.dataset.SalesforceDatasetProperties;
import org.talend.components.salesforce.datastore.SalesforceDatastoreDefinition;
import org.talend.components.salesforce.datastore.SalesforceDatastoreProperties;

public class SalesforceInputTestIT {

    @Test
    public void testReaderForModule() {
        Reader reader = null;
        try {
            SalesforceInputProperties properties = createCommonSalesforceInputPropertiesForModule();
            properties.getDatasetProperties().selectColumnIds.setValue(Arrays.asList("IsDeleted", "Id"));

            SalesforceDataprepSource source = new SalesforceDataprepSource();
            source.initialize(null, properties);
            source.validate(null);
            reader = source.createReader(null);

            reader.start();
            IndexedRecord row = (IndexedRecord) reader.getCurrent();
            String id = (String) row.get(0);
            Assert.assertNotNull("id is null", id);

            reader.advance();
            row = (IndexedRecord) reader.getCurrent();
            Assert.assertNotNull("id is null", id);

            reader.close();

            // Map<String, Object> returnMap = reader.getReturnValues();
            // Assert.assertEquals(3, returnMap.get(ComponentDefinition.RETURN_TOTAL_RECORD_COUNT));
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Assert.fail(e.getMessage());
                }
            }
        }

    }

    @Test
    public void testTypeForModule() throws Exception {
        SalesforceInputProperties properties = createCommonSalesforceInputPropertiesForModule();

        SalesforceDataprepSource source = new SalesforceDataprepSource();
        source.initialize(null, properties);
        source.validate(null);

        properties.getDatasetProperties().selectColumnIds.setValue(Arrays.asList("IsDeleted", "Id"));

        try (Reader reader = source.createReader(null)) {
            int count = 3;
            for (boolean available = reader.start(); available; available = reader.advance()) {
                IndexedRecord record = (IndexedRecord) reader.getCurrent();

                assertEquals(String.class, record.get(0).getClass());
                assertEquals(String.class, record.get(1).getClass());

                if ((count--) < 1) {
                    break;
                }
            }

        }
    }

    @Test(expected = RuntimeException.class)
    public void testTypeForModuleWithBadQuery() throws Exception {
        SalesforceInputProperties properties = createCommonSalesforceInputPropertiesForModule();

        SalesforceDataprepSource source = new SalesforceDataprepSource();
        source.initialize(null, properties);
        source.validate(null);

        properties.getDatasetProperties().selectColumnIds.setValue(Arrays.asList("Toto"));

        try (Reader reader = source.createReader(null)) {
            reader.start();
        }
    }

    @Test
    public void testTypeForModuleWithoutSpecifiedFields() throws Exception {
        SalesforceInputProperties properties = createCommonSalesforceInputPropertiesForModule();

        SalesforceDataprepSource source = new SalesforceDataprepSource();
        source.initialize(null, properties);
        source.validate(null);
        properties.getDatasetProperties().selectColumnIds.setValue(Arrays.asList("IsDeleted", "Id", "Name"));

        try (Reader reader = source.createReader(null)) {
            int count = 3;
            for (boolean available = reader.start(); available; available = reader.advance()) {
                IndexedRecord record = (IndexedRecord) reader.getCurrent();

                assertEquals(3, record.getSchema().getFields().size());
                if ((count--) < 1) {
                    break;
                }
            }

        }
    }

    @Test(expected = ComponentException.class)
    public void testTypeForModuleWithCompoundType() throws Exception {
        SalesforceInputProperties properties = createCommonSalesforceInputPropertiesForModule();

        SalesforceDataprepSource source = new SalesforceDataprepSource();
        source.initialize(null, properties);
        source.validate(null);
        properties.getDatasetProperties().selectColumnIds
                .setValue(Arrays.asList("IsDeleted", "Id", "Name", "BillingAddress", "ShippingAddress"));

        try (Reader reader = source.createReader(null)) {
            reader.start();
        }
    }

    @Test
    public void testTypeForModuleWithPickListType() throws Exception {
        SalesforceInputProperties properties = createCommonSalesforceInputPropertiesForModule();

        SalesforceDataprepSource source = new SalesforceDataprepSource();
        source.initialize(null, properties);
        source.validate(null);
        properties.getDatasetProperties().selectColumnIds.setValue(Arrays.asList("IsDeleted", "Id", "Name", "Type"));

        try (Reader reader = source.createReader(null)) {
            int count = 3;
            for (boolean available = reader.start(); available; available = reader.advance()) {
                IndexedRecord record = (IndexedRecord) reader.getCurrent();

                assertEquals(4, record.getSchema().getFields().size());
                if ((count--) < 1) {
                    break;
                }
            }

        }
    }

    @Test
    public void testReaderForQuery() {
        Reader reader = null;
        try {
            SalesforceInputProperties properties = createCommonSalesforceInputPropertiesForQuery();

            SalesforceDataprepSource source = new SalesforceDataprepSource();
            source.initialize(null, properties);
            source.validate(null);
            reader = source.createReader(null);

            reader.start();
            IndexedRecord row = (IndexedRecord) reader.getCurrent();
            String id = (String) row.get(0);
            String name = (String) row.get(1);
            Assert.assertNotNull("id is null", id);

            reader.advance();
            row = (IndexedRecord) reader.getCurrent();
            Assert.assertNotNull("id is null", id);

            reader.close();

            // Map<String, Object> returnMap = reader.getReturnValues();
            // Assert.assertEquals(3, returnMap.get(ComponentDefinition.RETURN_TOTAL_RECORD_COUNT));
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Assert.fail(e.getMessage());
                }
            }
        }

    }

    @Test
    public void testTypeForQuery() throws Exception {
        SalesforceInputProperties properties = createCommonSalesforceInputPropertiesForQuery();

        SalesforceDataprepSource source = new SalesforceDataprepSource();
        source.initialize(null, properties);
        source.validate(null);

        try (Reader reader = source.createReader(null);) {
            int count = 3;
            for (boolean available = reader.start(); available; available = reader.advance()) {
                IndexedRecord record = (IndexedRecord) reader.getCurrent();

                assertEquals(String.class, record.get(0).getClass());
                assertEquals(String.class, record.get(1).getClass());

                if ((count--) < 1) {
                    break;
                }
            }

        }
    }

    public void testLimitOfSalesforceBulQueryReader() throws Exception {
        SalesforceInputProperties properties = createCommonSalesforceInputPropertiesForModule();

        SalesforceDataprepSource source = new SalesforceDataprepSource();
        source.initialize(null, properties);
        source.validate(null);
        properties.getDatasetProperties().selectColumnIds.setValue(Arrays.asList("IsDeleted", "Id", "Name"));

        try (Reader reader = source.createReader(null);) {
            ((SalesforceBulkQueryReader) reader).setLimit(1);

            reader.start();
            reader.advance();

            IndexedRecord record = (IndexedRecord) reader.getCurrent();

            assertEquals(3, record.getSchema().getFields().size());
            try {
                reader.advance();
                fail();
            } catch (IOException e) {
                // Excepted to happen
            }
        }
    }

    private SalesforceInputProperties createCommonSalesforceInputPropertiesForModule() {
        SalesforceDatastoreDefinition datastore_def = new SalesforceDatastoreDefinition();
        SalesforceDatastoreProperties datastore_props = new SalesforceDatastoreProperties("datastore");

        CommonTestUtils.setValueForDatastoreProperties(datastore_props);

        SalesforceDatasetProperties dataset = (SalesforceDatasetProperties) datastore_def
                .createDatasetProperties(datastore_props);
        dataset.moduleName.setValue("Account");

        SalesforceInputDefinition input_def = new SalesforceInputDefinition();
        SalesforceInputProperties input_props = (SalesforceInputProperties) input_def.createRuntimeProperties();
        input_props.setDatasetProperties(dataset);
        List<String> products = input_def.getSupportedProducts();
        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals(SupportedProduct.DATAPREP, products.get(0));

        return input_props;
    }

    private SalesforceInputProperties createCommonSalesforceInputPropertiesForQuery() {
        SalesforceDatastoreDefinition datastore_def = new SalesforceDatastoreDefinition();
        SalesforceDatastoreProperties datastore_props = new SalesforceDatastoreProperties("datastore");

        CommonTestUtils.setValueForDatastoreProperties(datastore_props);

        SalesforceDatasetProperties dataset = (SalesforceDatasetProperties) datastore_def
                .createDatasetProperties(datastore_props);
        dataset.sourceType.setValue(SalesforceDatasetProperties.SourceType.SOQL_QUERY);
        dataset.query.setValue("SELECT Id, Name FROM Account");

        SalesforceInputDefinition input_def = new SalesforceInputDefinition();
        SalesforceInputProperties input_props = (SalesforceInputProperties) input_def.createRuntimeProperties();
        input_props.setDatasetProperties(dataset);

        return input_props;
    }

}
