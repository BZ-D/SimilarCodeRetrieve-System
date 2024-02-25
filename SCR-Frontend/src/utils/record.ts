const RECORD_PREFIX = 'RETRIEVE_RECORD_';

export function saveRecord(recordId: number, record: any) {
  localStorage.setItem(`${RECORD_PREFIX}${recordId}`, JSON.stringify(record));
}

export function getRecord(recordId: number | string) {
  return JSON.parse(localStorage.getItem(`${RECORD_PREFIX}${recordId}`));
}

export function removeRecord(recordId: number) {
  localStorage.removeItem(`${RECORD_PREFIX}${recordId}`);
}
