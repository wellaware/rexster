package com.tinkerpop.rexster.extension;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;

public class ExtensionConfigurationTest {

    private Mockery mockery = new JUnit4Mockery();

    @Test
    public void isExtensionAllowedAllowAll() {
        ExtensionConfiguration configuration = new ExtensionConfiguration("*:*");
        ExtensionSegmentSet extensionSegmentSet = new ExtensionSegmentSet(this.mockTheUri("ns", "extension", ""));
        Assert.assertTrue(configuration.isExtensionAllowed(extensionSegmentSet));
    }

    @Test
    public void isExtensionAllowedAllowAllInNamespace() {
        ExtensionConfiguration configuration = new ExtensionConfiguration("ns:*");
        ExtensionSegmentSet extensionSegmentSet = new ExtensionSegmentSet(this.mockTheUri("ns", "extension", ""));
        Assert.assertTrue(configuration.isExtensionAllowed(extensionSegmentSet));

        extensionSegmentSet = new ExtensionSegmentSet(this.mockTheUri("bs", "extension", ""));
        Assert.assertFalse(configuration.isExtensionAllowed(extensionSegmentSet));
    }

    @Test
    public void isExtensionAllowedAllowSpecificExtension() {
        ExtensionConfiguration configuration = new ExtensionConfiguration("ns:allowed");
        ExtensionSegmentSet extensionSegmentSet = new ExtensionSegmentSet(this.mockTheUri("ns", "allowed", ""));
        Assert.assertTrue(configuration.isExtensionAllowed(extensionSegmentSet));

        extensionSegmentSet = new ExtensionSegmentSet(this.mockTheUri("ns", "not-allowed", ""));
        Assert.assertFalse(configuration.isExtensionAllowed(extensionSegmentSet));
    }

    private UriInfo mockTheUri(final String namespace, final String extension, final String method) {
        this.mockery = new JUnit4Mockery();

        final UriInfo uri = this.mockery.mock(UriInfo.class);
        final List<PathSegment> pathSegments = new ArrayList<PathSegment>();
        final PathSegment graphPathSegment = this.mockery.mock(PathSegment.class, "graphPathSegment");
        final PathSegment namespacePathSegment = this.mockery.mock(PathSegment.class, "namespacePathSegment");
        final PathSegment extensionPathSegment = this.mockery.mock(PathSegment.class, "extensionPathSegment");
        final PathSegment methodPathSegment = this.mockery.mock(PathSegment.class, "methodPathSegment");

        pathSegments.add(graphPathSegment);
        pathSegments.add(namespacePathSegment);
        pathSegments.add(extensionPathSegment);
        pathSegments.add(methodPathSegment);

        this.mockery.checking(new Expectations() {{
            allowing(namespacePathSegment).getPath();
            will(returnValue(namespace));
            allowing(extensionPathSegment).getPath();
            will(returnValue(extension));
            allowing(methodPathSegment).getPath();
            will(returnValue(method));
            allowing(uri).getPathSegments();
            will(returnValue(pathSegments));
        }});

        return uri;
    }
}