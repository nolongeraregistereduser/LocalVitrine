import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { TemplateDto } from '../../../services/template.service';

@Component({
  selector: 'app-template-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './template-card.component.html',
  styleUrl: './template-card.component.scss'
})
export class TemplateCardComponent {
  @Input({ required: true }) template!: TemplateDto;
  @Input() selected = false;
  @Input() disabled = false;
  @Input() selectPulse = false;

  @Output() select = new EventEmitter<TemplateDto>();

  imageError = false;

  onSelect(): void {
    if (this.disabled) {
      return;
    }
    this.select.emit(this.template);
  }

  onImgError(): void {
    this.imageError = true;
  }
}
