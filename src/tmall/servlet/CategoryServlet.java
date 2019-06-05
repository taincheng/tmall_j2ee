package tmall.servlet;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tmall.DAO.CategoryDAO;
import tmall.bean.Category;
import tmall.util.ImageUtil;
import tmall.util.Page;

public class CategoryServlet extends BaseBackServlet {
    @Override
    public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
        Map<String,String> params = new HashMap<>();
        InputStream is = super.parseUpload(request,params);

        String name = params.get("name");
        Category category = new Category();
        category.setName(name);
        categoryDAO.add(category);

        File imgFolder= new File(request.getSession().getServletContext().getRealPath("img/category"));
        File file = new File(imgFolder,category.getId() + ".jpg");
        try {
            if(null != is && 0 != is.available()){
                try (FileOutputStream fos = new FileOutputStream(file)){
                    byte[] bytes = new byte[1024 * 10240];
                    int len = 0;
                    while(-1 != (len = is.read(bytes))){
                        fos.write(bytes,0,len);
                    }
                    fos.flush();
                    BufferedImage bufferedImage = ImageUtil.change2jpg(file);
                    ImageIO.write(bufferedImage,"jpg",file);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return "@admin_category_list";
    }

    @Override
    public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
        int id = Integer.parseInt(request.getParameter("id"));
        categoryDAO.delete(id);
        return "@admin_category_list";
    }

    @Override
    public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
        int id = Integer.parseInt(request.getParameter("id"));
        Category c = categoryDAO.get(id);
        request.setAttribute("c",c);
        return "admin/editCategory.jsp";
    }

    @Override
    public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
        Map<String,String> params = new HashMap<>();
        InputStream is = super.parseUpload(request,params);

        String name = params.get("name");
        int id = Integer.parseInt(request.getParameter("id"));
        Category c = new Category();
        c.setName(name);
        c.setId(id);

        categoryDAO.update(c);

        File imgFolder = new File(request.getSession().getServletContext().getRealPath("img/category"));
        File file = new File(imgFolder,c.getId()+".jpg");
        file.getParentFile().mkdirs();

        try {
            if(null != is && 0 != is.available()){
                try (FileOutputStream fos = new FileOutputStream(file)){
                    byte[] bytes = new byte[1024*10240];
                    int len = 0;
                    while(-1!=(len = is.read(bytes))){
                        fos.write(bytes,0,len);

                    }
                    fos.flush();
                    BufferedImage imgbuf = ImageUtil.change2jpg(file);
                    ImageIO.write(imgbuf,"jpg",file);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "@admin_category_list";
    }

    //查询功能
    @Override
    public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
        List<Category> cs = categoryDAO.list(page.getStart(),page.getCount());
        int total = categoryDAO.getTotal();
        page.setTotal(total);
        request.setAttribute("thecs",cs);
        request.setAttribute("page",page);
        return "admin/listCategory.jsp";
    }
}
