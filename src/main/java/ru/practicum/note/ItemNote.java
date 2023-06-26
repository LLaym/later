package ru.practicum.note;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.item.Item;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "item_notes")
@Getter @Setter @ToString
public class ItemNote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text", length = 2000, nullable = false)
    private String text;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(name = "created", nullable = false)
    private LocalDateTime dateOfNote;
}
