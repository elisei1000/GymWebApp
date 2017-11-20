package com.gymwebapp.domain;


import javax.persistence.*;

@Entity
@Table(name="Administrator", uniqueConstraints = @UniqueConstraint(columnNames = {"username"}))
public class Administrator extends User{




}
