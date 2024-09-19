package dev.carlosandrade.myapp.entity;

import lombok.Data;
import javax.persistence.*;

@Entity
@Data
@Table(name = "sysmodules")
public class SysModuleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
}
