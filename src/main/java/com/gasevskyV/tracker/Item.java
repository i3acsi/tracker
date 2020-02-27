package com.gasevskyV.tracker;

import java.util.Date;

/**
 * Класс  Item описывают бизнес модель заявки.
 *
 * @author Vasiliy Gasevskiy (gasevskyv@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class Item {
    /**
     * Название задачи
     */
    private String task;

    /**
     * Описание задачи
     */
    private String description;

    /**
     * Время создания задачи
     */
    private long dateCreation;

    /**
     * Комментарии к  задаче
     */
    private String comments;

    /**
     * Id задачи
     */
    private String id;

    /**
     * Переопределим метод hashcode.
     *
     * @return hashcode элемента.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) this.getDateCreation();
        result = prime * result + this.getTask().hashCode();
        return result;
    }

    /**
     * Переопределим метод equals.
     *
     * @return boolean - результат сравнения двух элементов.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Item other = (Item) obj;
        return (this.getTask().equals(other.getTask())
                && this.getDesk().equals(other.getDesk())
                && this.getDateCreation() == other.getDateCreation()
                && this.getId().equals(other.getId())
        );
    }

    /**
     * Конструктор - создание нового объекта с определенными значениями. dateCreation - текущее время.
     * Comments - комментарри.
     *
     * @param task - название задачи.
     * @param desc - описание задачи.
     */
    Item(String task, String desc) {
        this.task = task;
        this.description = desc;
        this.dateCreation = (new Date()).getTime();
        this.comments = "";
    }

    /**
     * Конструктор - создание нового объекта на основе уже существующего.
     */
    public Item(Item item) {
        this.task = item.getTask();
        this.description = item.getDesk();
        this.dateCreation = item.dateCreation;
        this.id = item.getId();
        this.comments = item.getComments();
    }

    /**
     * Процедура определения названия задачи.
     *
     * @param task - название задачи.
     */
    public void setTask(String task) {
        this.task = task;
    }

    /**
     * Процедура определения описания задачи.
     *
     * @param description - описание задачи.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Процедура определения времени создания задачи по уже имеющимя данным.
     *
     * @param dateCreation - время создания задачи.
     */
    public void setDateCreation(long dateCreation) {
        this.dateCreation = dateCreation;
    }

    /**
     * Процедура определения id задачи.
     *
     * @param id - id задачи.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Процедура определения комментариев к задаче.
     *
     * @param comment - название задачи.
     */
    public void setComments(String comment) {
        this.comments = comment;
    }

    /**
     * Функция получения значения поля названия задачи.
     *
     * @return task - возвращает название задачи.
     */
    public String getTask() {
        return this.task;
    }

    /**
     * Функция получения значения поля описания задачи.
     *
     * @return description - возвращает описание задачи.
     */
    public String getDesk() {
        return this.description;
    }

    /**
     * Функция получения значения поля времени создания задачи.
     *
     * @return dateCreation - возвращает время создания задачи.
     */
    public long getDateCreation() {
        return this.dateCreation;
    }

    /**
     * Функция получения значения поля комментариев к задаче.
     *
     * @return comments - возвращает комментарии к задаче.
     */
    public String getComments() {
        return this.comments;
    }

    /**
     * Функция получения значения поля id задачи.
     *
     * @return id - возвращает id задачи.
     */
    public String getId() {
        return this.id;
    }

    public String show() {
        return "Task: " + this.task
                + ". Description: " + this.description
                + ". ID: " + this.id
                + ". Date: " + (new Date(this.dateCreation).toString())
                + ". Comments: " + this.comments;
    }
}
