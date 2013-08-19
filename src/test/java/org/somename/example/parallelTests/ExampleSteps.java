package org.somename.example.parallelTests;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

public class ExampleSteps
{
    private String applicationId;
    private Object applicationState;

    @Given("an application has an ID of <id>")
    public void givenAnApplicationWithIdOf(String id)
    {
        applicationId = id;
    }

    @When("I check its state")
    public void whenICheckItsState()
    {
        applicationState = queryDatabaseForApplicationState(applicationId);
    }

    @Then("the application should be showing as <normalstate>")
    public void thenApplicationShouldBeShowingAs(String normalstate)
    {
        // Assert that the applicationState matches the passed in state
    }

    @Then("the default action should be <defaultstate>")
    public void thenTheDefaultActionShouldBe(String defaultstate)
    {
        // Assert that the application default action matches the passed in default action
    }

    private Object queryDatabaseForApplicationState(String applicationId)
    {
        //TODO Actually implement
        return null;
    }

}
