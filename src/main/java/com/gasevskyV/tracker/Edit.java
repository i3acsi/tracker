package com.gasevskyV.tracker;

/**
 * Класс для редактирования заявки.
 */
public class Edit extends BaseAction {

    public Edit(int key, String info) {
        super(key, info);
    }

    @Override


    public void execute(Input input, ITracker tracker) {
        System.out.println("----------Редактирование заявки----------");
        String id = input.ask("Введите id редактируемой заявки ");
        Item temp = tracker.findById(id);
        if (temp != null) {
            String task = input.ask("Введите новое название заявки ");
            String desc = input.ask("Введите новое описание заявки ");
            String newComment = null;
            if (temp.getComments()!=null&&!temp.getComments().equals("")) {
                newComment = input.ask("Введите новый коииентарий к заявке ");
            }
            System.out.println("------------------------------------------");
            Item item = new Item(task, desc);
            if (newComment != null) {
                item.setComments(newComment);
            }
            tracker.replace(id, item);

            System.out.print("Заявка отредактированна." + item.show() + System.lineSeparator());
        } else {
            System.out.println("---------Заявка не найдена---------");
        }
    }
}