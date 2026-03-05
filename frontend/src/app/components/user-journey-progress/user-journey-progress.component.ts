import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { JourneyProgressVm } from '../../services/user-journey.service';

@Component({
  selector: 'app-user-journey-progress',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user-journey-progress.component.html',
  styleUrl: './user-journey-progress.component.scss'
})
export class UserJourneyProgressComponent {
  @Input() progress: JourneyProgressVm | null = null;
  @Input() compact = false;
}

