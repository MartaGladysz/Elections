package com.app.model;

import lombok.*;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Constituency {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private int votes;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "constituency")
    private Set<Candidate> candidates = new HashSet<>();

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "constituency")
    private Set<Voter> voters = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Constituency that = (Constituency) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Constituency{" +
                "id=" + id +
                ", name='" + name;
    }

    public void resetVotes() {

        this.votes = 0;
    }
}