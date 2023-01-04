package com.example.exploro;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class RegistrationUnitTest {
    @Test
    public void validEmail(){
        HelpFunctions helpFunctions = new HelpFunctions();
        assertTrue(helpFunctions.validEmail("test@email.com"));
    }
    @Test
    public void invalidEmail(){
        HelpFunctions helpFunctions = new HelpFunctions();
        assertFalse(helpFunctions.validEmail("testemail.com"));
    }
    @Test
    public void validPasswordUppercase(){
        HelpFunctions helpFunctions = new HelpFunctions();
        assertTrue(helpFunctions.validPasswordUppercase("Test"));
    }
    @Test
    public void invalidPasswordUppercase(){
        HelpFunctions helpFunctions = new HelpFunctions();
        assertFalse(helpFunctions.validPasswordUppercase("test"));
    }
    @Test
    public void validPasswordDigit(){
        HelpFunctions helpFunctions = new HelpFunctions();
        assertTrue(helpFunctions.validPasswordDigit("test1"));
    }
    @Test
    public void invalidPasswordDigit(){
        HelpFunctions helpFunctions = new HelpFunctions();
        assertFalse(helpFunctions.validPasswordDigit("test"));
    }
    @Test
    public void validPasswordSpecial(){
        HelpFunctions helpFunctions = new HelpFunctions();
        assertTrue(helpFunctions.validPasswordSpecial("!test"));
    }
    @Test
    public void invalidPasswordSpecial(){
        HelpFunctions helpFunctions = new HelpFunctions();
        assertFalse(helpFunctions.validPasswordSpecial("test"));
    }
    @Test
    public void validPasswordLength(){
        HelpFunctions helpFunctions = new HelpFunctions();
        assertTrue(helpFunctions.validPasswordLength("Test1234!"));
    }
    @Test
    public void invalidPasswordLength(){
        HelpFunctions helpFunctions = new HelpFunctions();
        assertFalse(helpFunctions.validPasswordLength("te!123"));
    }
    @Test
    public void validPasswordConfirm(){
        HelpFunctions helpFunctions = new HelpFunctions();
        assertTrue(helpFunctions.validPasswordConfirm("Test123!", "Test123!"));
    }
    @Test
    public void invalidPasswordConfirm(){
        HelpFunctions helpFunctions = new HelpFunctions();
        assertFalse(helpFunctions.validPasswordConfirm("Test123!", "Test123!!"));
    }
    @Test
    public void validNonEmptyFields(){
        HelpFunctions helpFunctions = new HelpFunctions();
        assertTrue(helpFunctions.nonEmptyField("test","test@test.com","test","Test123!", "Test123!"));
    }
    @Test
    public void invalidNonEmptyFields(){
        HelpFunctions helpFunctions = new HelpFunctions();
        assertFalse(helpFunctions.nonEmptyField("test","","test","Test123!", "Test123!"));
    }
}