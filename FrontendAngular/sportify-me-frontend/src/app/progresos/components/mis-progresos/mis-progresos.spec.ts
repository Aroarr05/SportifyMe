import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MisProgresos } from './mis-progresos';

describe('MisProgresos', () => {
  let component: MisProgresos;
  let fixture: ComponentFixture<MisProgresos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MisProgresos]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MisProgresos);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
