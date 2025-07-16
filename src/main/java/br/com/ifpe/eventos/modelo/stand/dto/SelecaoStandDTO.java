package br.com.ifpe.eventos.modelo.stand.dto;

import java.util.List;

public class SelecaoStandDTO {
    private Long eventId;
    private List<Long> standIds;
    private Long userId;

    // Getters e Setters
    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public List<Long> getStandIds() {
        return standIds;
    }

    public void setStandIds(List<Long> standIds) {
        this.standIds = standIds;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}