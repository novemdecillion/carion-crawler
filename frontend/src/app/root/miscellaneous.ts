import { format, parseISO } from "date-fns";

export function formatGraphQLDateTime(dateTime: any) {
  return format(parseISO(dateTime), 'yyyy-MM-dd HH:mm');
}
