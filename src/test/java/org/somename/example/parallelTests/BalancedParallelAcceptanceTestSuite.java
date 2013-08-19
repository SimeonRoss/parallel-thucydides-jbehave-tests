package org.somename.example.parallelTests;

import ch.lambdaj.Lambda;
import net.thucydides.jbehave.ThucydidesJUnitStories;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BalancedParallelAcceptanceTestSuite extends ThucydidesJUnitStories
{
    public BalancedParallelAcceptanceTestSuite()
    {
        Integer agentPosition = Integer.parseInt(System.getProperty("parallel.agent.number"));
        Integer agentCount = Integer.parseInt(System.getProperty("parallel.agent.total"));
        List<String> storyPaths = storyPaths();

        failIfAgentIsNotConfiguredCorrectly(agentPosition, agentCount);
        failIfThereAreMoreAgentsThanStories(agentPosition, agentCount, storyPaths.size());

        List<String> stories = getThisAgentsStories(storyPaths, agentPosition, agentCount);

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
        } else if (agentPosition > agentCount)
        {
            throw new RuntimeException(String.format("There were %d agents in total specified and this agent is outside that range (it is specified as %d)", agentPosition, agentCount));
        }
    }

    private void failIfThereAreMoreAgentsThanStories(Integer agentPosition, Integer agentCount, int storyCount)
    {
        if (storyCount < agentCount && agentPosition > storyCount)
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

    private List<String> getThisAgentsStories(List<String> storyPaths, Integer agentPosition, Integer agentCount)
    {
        Collections.sort(storyPaths, new StringFilenameSizeComparator());

        List<String> agentStories = new ArrayList<String>();
        for (int i = agentPosition - 1; i < storyPaths.size(); i = i + agentCount)
        {
            agentStories.add(storyPaths.get(i));
        }

        return agentStories;
    }

    private class StringFilenameSizeComparator implements Comparator<String>
    {
        public int compare(String o1, String o2)
        {
            Long l1 = new File(getClassLoader().getResource(o1).getFile()).length();
            Long l2 = new File(getClassLoader().getResource(o2).getFile()).length();
            return l1.compareTo(l2) * -1;
        }
    }

}