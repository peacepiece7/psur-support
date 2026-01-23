export default defineEventHandler((event) => {
  const ip =
    getRequestHeader(event, 'x-forwarded-for')?.split(',')[0] ??
    event.node.req.socket.remoteAddress

  event.context.clientIp = ip
})
