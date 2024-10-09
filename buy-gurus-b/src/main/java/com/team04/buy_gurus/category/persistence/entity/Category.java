package com.team04.buy_gurus.category.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;


    //부모삭제시 자식도 함께 삭제됩니다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category parent;

    public Category(final String name, final Category parent){
        this.name = name;
        this.parent = parent;
    }

}
