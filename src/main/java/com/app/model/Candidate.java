package com.app.model;


import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Candidate {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String surname;
    private int votes;
    private boolean active;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "constituency_id")
    private Constituency constituency;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Candidate candidate = (Candidate) o;

        return Objects.equals(id, candidate.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Candidate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", votes=" + votes +
                ", constituency=" + constituency.getName() +
                '}';
    }

    public void resetVotes() {

        this.votes = 0;
    }

}

