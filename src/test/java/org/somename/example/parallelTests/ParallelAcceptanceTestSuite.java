package org.somename.example.parallelTests;

import ch.lambdaj.Lambda;
import net.thucydides.jbehave.ThucydidesJUnitStories;

import java.util.List;

public class ParallelAcceptanceTestSuite extends ThucydidesJUnitStories {

    public ParallelAcceptanceTestSuite() {
        Integer agentPosition = Integer.parseInt(System.getProperty("parallel.agent.number"));
        Integer agentCount = Integer.parseInt(System.getProperty("parallel.agent.total"));
        List storyPaths = storyPaths();

        failIfAgentIsNotConfiguredCorrectly(agentPosition, agentCount);
        failIfThereAreMoreAgentsThanStories(agentCount, storyPaths.size());

        int reminder = storyPaths.size() % agentCount; // The reminder should work out to be either be zero or one.
        int storiesPerAgent = storyPaths.size() / agentCount;

        int startPos = storiesPerAgent * (agentPosition - 1);
        int endPos = startPos + storiesPerAgent;
        if (agentPosition == agentCount)
        {
            // In the case of an uneven number the last agent picks up the extra story file.
            endPos += reminder;
        }
        List stories = storyPaths.subList(startPos, endPos);

        outputWhichStoriesAreBeingRun(stories);
        findStoriesCalled(Lambda.join(stories, ";"));
    }

    private void failIfAgentIsNotConfiguredCorrectly(Integer agentPosition, Integer agentCount)
    {
        if (agentPosition == null)
        {
            throw new RuntimeException("The agent number needs to be specified");
        } else if (agentCount == null)
        {
            throw new RuntimeException("The agent total needs to be specified");
        } else if (agentPosition < 1)
        {
            throw new RuntimeException("The agent number is invalid");
        } else if (agentCount < 1)
        {
            throw new RuntimeException("The agent total is invalid");
        } else if (agentPosition > agentCount )
        {
            throw new RuntimeException(String.format("There were %d agents in total specified and this agent is outside that range (it is specified as %d)", agentPosition, agentCount));
        }
    }

    private void failIfThereAreMoreAgentsThanStories(Integer agentCount, int storyCount)
    {
        if (storyCount > agentCount)
        {
            throw new RuntimeException("There are more agents then there are stories, this agent isn't necessary");
        }
    }

    private void outputWhichStoriesAreBeingRun(List<String> stories)
    {
        System.out.println("Running stories: ");
        for (String story : stories)
        {
            System.out.println(" - " + story);
        }
    }

}