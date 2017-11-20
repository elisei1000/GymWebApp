package com.gymwebapp.domain;

import javax.persistence.*;

/**
 * Created by elisei on 20.11.2017.
 */
@Entity
@Table(name="Client", uniqueConstraints = @UniqueConstraint(columnNames = {"username"}))
public class Client extends User {
}
