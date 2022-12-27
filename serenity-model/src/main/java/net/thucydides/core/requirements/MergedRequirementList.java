package net.thucydides.core.requirements;

import net.thucydides.core.requirements.model.Requirement;

import java.util.ArrayList;

public class MergedRequirementList extends ArrayList<Requirement> {

    @Override
    public boolean add(Requirement newRequirement) {
        if (!this.contains(newRequirement)) {
            return super.add(newRequirement);
        } else {
            Requirement existing = this.remove(this.indexOf(newRequirement));
            return super.add(existing.merge(newRequirement));
        }
    }
}
