package org.talend.components.salesforce.test;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.talend.components.base.ComponentProperties;
import org.talend.components.base.ComponentService;
import org.talend.components.base.properties.presentation.Form;
import org.talend.components.base.test.testcomponent.TestComponentDefinition;
import org.talend.components.base.test.testcomponent.TestComponentProperties;
import org.talend.components.common.OauthProperties;
import org.talend.components.common.UserPasswordProperties;
import org.talend.components.salesforce.SalesforceConnectionProperties;
import org.talend.components.salesforce.tsalesforceconnect.TSalesforceConnectDefinition;
import org.talend.components.salesforce.tsalesforceconnect.TSalesforceConnectProperties;

@RunWith(SpringJUnit4ClassRunner.class) @ContextConfiguration(classes = { ComponentService.class,
        TSalesforceConnectDefinition.class }, loader = AnnotationConfigContextLoader.class) public class SalesforceLocalComponentTest
        extends TestCase {

    @Autowired protected ComponentService componentService;

    public SalesforceLocalComponentTest() {
    }

    @Test public void testGetProps() {
        ComponentProperties props = componentService.getComponentProperties(TSalesforceConnectDefinition.COMPONENT_NAME);
        Form f = props.getForm(TSalesforceConnectProperties.CONNECTION);
        System.out.println(f);
        System.out.println(props);
    }

    @Test public void testAfterLoginType() {
        SalesforceConnectionProperties props;
        Form f;

        props = (SalesforceConnectionProperties) componentService
                .getComponentProperties(TSalesforceConnectDefinition.COMPONENT_NAME);
        assertEquals(SalesforceConnectionProperties.LoginType.BASIC, props.loginType.getValue());
        f = props.getForm(TSalesforceConnectProperties.CONNECTION);
        assertTrue(f.getLayout(UserPasswordProperties.USERPASSWORD).isVisible());
        assertFalse(f.getLayout(OauthProperties.OAUTH).isVisible());

        assertTrue(f.getLayout("loginType").isCallAfter());
        props.loginType.setValue(SalesforceConnectionProperties.LoginType.OAUTH);

        props = (SalesforceConnectionProperties) componentService.afterProperty("loginType", props);
        f = props.getForm(TSalesforceConnectProperties.CONNECTION);
        assertTrue(f.isRefreshUI());

        assertFalse(f.getLayout(UserPasswordProperties.USERPASSWORD).isVisible());
        assertTrue(f.getLayout(OauthProperties.OAUTH).isVisible());

    }

}