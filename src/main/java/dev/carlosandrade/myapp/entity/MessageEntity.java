package dev.carlosandrade.myapp.entity;

import java.time.LocalDateTime;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "messages")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MessageEntity
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectEntity project;

    @Lob
    private String message;

    private String sender; // "user" ou "bot"

    private LocalDateTime timestamp;

    // Getters e Setters
}
