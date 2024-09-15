package inovaEmpresa.builder;

import inovaEmpresa.entities.Idea;

public class IdeaBuilder {
    private Long id;
    private String name;
    private String impact;
    private double estimatedCost;
    private String description;

    public IdeaBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public IdeaBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public IdeaBuilder withImpact(String impact) {
        this.impact = impact;
        return this;
    }

    public IdeaBuilder withEstimatedCost(double estimatedCost) {
        this.estimatedCost = estimatedCost;
        return this;
    }

    public IdeaBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public Idea build() {
        Idea idea = new Idea();
        idea.setId(id);
        idea.setName(name);
        idea.setImpact(impact);
        idea.setEstimatedCost(estimatedCost);
        idea.setDescription(description);
        return idea;
    }
}