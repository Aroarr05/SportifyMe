import { TestBed } from '@angular/core/testing';

import { Progresos } from './progresos';

describe('Progresos', () => {
  let service: Progresos;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Progresos);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
