package me.mrxbox98.Jast.core;

import com.google.gson.Gson;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class JastTest {

    /**
     * Method to call
     */
    Method method;

    /**
     * Expected values
     */
    Object[] expected;

    /**
     * Description of the test
     */
    String description;

    /**
     * Parameters to call the method with
     */
    Object[] parameters;

    /**
     * The caller of the method - Not required for static methods
     */
    Object caller;

    /**
     * The name of the method
     */
    String name;

    /**
     * How long the method should take
     */
    long time;

    /**
     * Check for strictly equals instead of Object#equals(Object obj)
     */
    boolean strictEquals;

    /**
     * Creates a new JastTest
     */
    public JastTest()
    {

    }

    /**
     * Sets the expected values
     * @param expected The expected values
     * @return the current object for chaining
     */
    public JastTest setExpected(Object... expected)
    {
        this.expected=expected;
        return this;
    }

    /**
     * Sets the description of the object
     * @param description the description
     * @return the current object for chaining
     */
    public JastTest setDescription(String description)
    {
        this.description=description;
        return this;
    }

    /**
     * Sets the test's name
     * @param name the name of the test
     * @return the current object for chaining
     */
    public JastTest setName(String name)
    {
        this.name=name;
        return this;
    }

    /**
     * Sets the parameters of the call
     * @param params the parameters of the object call
     * @return the current object for chaining
     */
    public JastTest setParameters(Object... params)
    {
        this.parameters=params;
        return this;
    }

    /**
     * Sets the calling object of the
     * @param caller of the method
     * @return the current object for chaining
     */
    public JastTest setCaller(Object caller)
    {
        this.caller=caller;
        return this;
    }

    /**
     * Sets the method to call
     * @param method the method to call
     * @return the current object for chaining
     */
    public JastTest setMethod(Method method)
    {
        this.method=method;
        return this;
    }

    /**
     * Sets the time the method should take to complete
     * @param time the time it should take
     * @return the current object for chaining
     */
    public JastTest setTime(long time)
    {
        this.time=time;
        return this;
    }

    /**
     * Sets whether the test should use == or .equals
     * @param strictEquals whether the test should use == or .equals
     * @return the current object for chaining
     */
    public JastTest setStrictEquals(boolean strictEquals)
    {
        this.strictEquals=strictEquals;
        return this;
    }

    /**
     * Tests the method
     * @return true if the method works and false otherwise
     */
    public boolean test()
    {
        return test(true);
    }

    /**
     * Tests the method
     * @param print Whether to print out or not
     * @return The test result
     */
    public boolean test(boolean print)
    {
        try {
            long currentTime = System.currentTimeMillis();

            Object ret = method.invoke(caller, parameters);

            long diff = System.currentTimeMillis()-currentTime;

            if(ret==null && expected.length==0)
            {
                if(print)
                {

                    printPass();
                    pass("Expected "+ Arrays.toString(expected) + " and got null ("+diff+"ms)");
                    printMethod();
                }
                return true;
            }

            for(Object e: expected)
            {
                if((strictEquals && e==ret) || (!strictEquals && e.equals(ret)))
                {
                    if(diff>time)
                    {
                        if(print)
                        {
                            printFail();
                            fail("Took too long ("+diff+"ms)");
                            printMethod();
                        }
                        return false;
                    }

                    if(print)
                    {

                        printPass();
                        pass("Expected "+ Arrays.toString(expected) + " and got "+ ret + " ("+diff+"ms)");
                        printMethod();
                    }
                    return true;
                }
            }
            if(print)
            {
                printFail();
                if(expected.length==0)
                {
                    fail("No expected values, but return type was not null");
                }
                else
                {
                    fail("Got "+ret+" but expected "+ Arrays.toString(expected));
                }
                printMethod();
            }
            return false;
        }
        catch (IllegalAccessException e)
        {
            if(print)
            {
                printFail();
                fail("NOT ABLE TO ACCESS METHOD");
                printMethod();
                return false;
            }
        }
        catch (InvocationTargetException e)
        {
            for(Object exception: expected)
            {
                if(exception.getClass().equals(e.getCause().getClass()))
                {
                    if(print)
                    {
                        printPass();
                        printMethod();
                    }
                    return true;
                }
            }
            if(print)
            {
                printFail();
                fail("UNEXPECTED ERROR");
                for(int i=0;i<e.getCause().getStackTrace().length;i++)
                {
                   fail(e.getCause().getStackTrace()[i].toString());
                }
                printMethod();
            }
            return false;
        }
        catch (Exception ignored)
        {

        }

        return false;
    }

    /**
     * Prints fail
     * BOLD
     * BLACK
     * BRIGHT RED BG
     */
    public static void printFail()
    {
        System.out.println("\u001b[1m\u001b[30m\u001b[41;1m FAIL \u001b[0m");
    }

    /**
     * Prints pass
     * BOLD
     * BLACK
     * BRIGHT GREEN BG
     */
    public static void printPass()
    {
        System.out.println("\u001b[1m\u001b[30m\u001b[42;1m PASS \u001b[0m");
    }

    public void printMethod()
    {
        System.out.println(method.getName());
        System.out.println(name);
        System.out.println(description);
    }

    /**
     * Prints a fail message with color coding
     * @param message message to print
     */
    public static void fail(String message)
    {
        System.out.println("\u001b[1m \u001b[31mX \u001b[37;1m " + message);
    }

    /**
     * Prints a pass message
     * @param message message to print
     */
    public static void pass(String message)
    {
        System.out.println("\u001b[1m \u001b[32;1m√ \u001b[37;1m " + message);
    }

    /**
     * Converts this test into a string that can be read by FileLoad
     * @see me.mrxbox98.Jast.file.FileLoad
     * @return the JSON version of this test
     */
    @Override
    public String toString()
    {
        return new Gson().toJson(this);
    }

}
