package dev.carlosandrade.myapp.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import dev.carlosandrade.myapp.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "technologies")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TechnologyEntity extends EntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String version;

    @ManyToOne
    @JoinColumn(name = "typetechnology_id", foreignKey = @ForeignKey(name = "fk_technologies_typetechnologies"))
    private TypeTechnologyEntity typeTechnology;

    @ManyToOne
    @JoinColumn(name = "sysmodule_id", foreignKey = @ForeignKey(name = "fk_technologies_sysmodules"))
    private SysModuleEntity sysModule;

}

