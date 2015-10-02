package com.vackosar.searchbasedlauncher.control;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RegexEscaperTest {

    private RegexEscaper regexEscaper;

    @Before
    public void init() {
        regexEscaper = new RegexEscaper();
    }

    @Test
    public void escape() {
        Assert.assertEquals("abc\\+\\{d", regexEscaper.act("abc+{d"));
    }
}
